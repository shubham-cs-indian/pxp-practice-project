package com.cs.core.runtime.interactor.usecase.runtimemapping;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.mapping.ColumnTagValueAutoMappingModel;
import com.cs.core.config.interactor.model.mapping.IColumnTagValueAutoMappingModel;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetTagValueFromColumnModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.TagValueMappingModel;
import com.cs.core.config.interactor.model.tag.GetTagModel;
import com.cs.core.config.interactor.model.tag.IGetTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.usecase.mapping.IGetMapping;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllMappedEndpointsStrategy;
import com.cs.core.config.strategy.usecase.tag.IGetTagStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.FileUtil;
import com.cs.core.runtime.interactor.utils.OffboardingUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class GetTagValuesFromColumn implements IGetTagValuesFromColumn {
  
  @Autowired
  protected String                         hotFolderPath;
  
  @Autowired
  protected IGetTagStrategy                getTagStrategy;
  
  @Autowired
  protected ISessionContext                context;
  
  @Autowired
  protected TransactionThreadData          transactionThread;
  
  @Autowired
  protected FileUtil                       fileUtil;
  
  @Override
  public IColumnTagValueAutoMappingModel execute(IGetTagValueFromColumnModel model) throws Exception
  {
    IColumnTagValueAutoMappingModel columnTagValueAutoMappingModel = new ColumnTagValueAutoMappingModel();
    if (model.getWorkBook() == null) {
      String filePath = hotFolderPath + "//" + model.getFileId();
      File file = new File(filePath);
      FileInputStream fileInputStream = new FileInputStream(file);
      XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
      model.setWorkBook(workbook);
    }
    String tagGroupId = model.getTagGroupId();
    IGetTagModel getTagModel = new GetTagModel(tagGroupId, null);
    ITagModel tagModel = getTagStrategy.execute(getTagModel);
    columnTagValueAutoMappingModel.setTag(tagModel);
    Set<String> tagValuesFromSheet = fileUtil.getTagValuesFromColumn(model.getWorkBook(), model);
    IIdsListParameterModel columnNames = new IdsListParameterModel();
    columnNames.setIds(new ArrayList<String>(tagValuesFromSheet));
    List<String> alreadyMapped = new ArrayList<>();
    
    IMappingModel propertyMapping = model.getMappingModel();
    
    List<IConfigRuleTagMappingModel> tagMappings = propertyMapping.getTagMappings();
    if (tagMappings != null && !tagMappings.isEmpty()) {
      for (IConfigRuleTagMappingModel tagMapping : tagMappings) {
        if (tagMapping.getMappedElementId()
            .equals(model.getTagGroupId())
            && tagMapping.getColumnNames()
                .contains(model.getColumnName())) {
          List<IColumnValueTagValueMappingModel> tagValueMappings = tagMapping
              .getTagValueMappings();
          if (tagValueMappings != null && !tagValueMappings.isEmpty()) {
            columnTagValueAutoMappingModel.setColumnName(tagValueMappings.get(0)
                .getColumnName());
            if (tagModel.getTagType()
                .equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
              tagValueMappings.get(0)
                  .setMappings(new ArrayList<>());
            }
            List<ITagValueMappingModel> mappingForAutoMapping = tagValueMappings.get(0)
                .getMappings();
            for (ITagValueMappingModel mapping : mappingForAutoMapping) {
              if (tagValuesFromSheet.contains(mapping.getTagValue())) {
                List<ITagValueMappingModel> singleMapping = columnTagValueAutoMappingModel
                    .getMappings();
                singleMapping.add(mapping);
                columnTagValueAutoMappingModel.setMappings(singleMapping);
                tagValuesFromSheet.remove(mapping.getTagValue());
                alreadyMapped.add(mapping.getMappedTagValueId());
              }
            }
          }
        }
      }
    }
    if (!tagValuesFromSheet.isEmpty() && !tagModel.getTagType()
        .equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
      Map<String, Object> tagValueMap = new HashMap<>();
      
      List<String> tagValueIds = new ArrayList<>();
      getTagValuesMap(tagModel, tagValueMap, tagValueIds, tagValuesFromSheet, alreadyMapped);
      List<ITagValueMappingModel> mappingForReturnModel = columnTagValueAutoMappingModel
          .getMappings();
      mappingForReturnModel.addAll(getAutoTagValueMapping(tagValuesFromSheet, tagValueMap));
      columnTagValueAutoMappingModel.setMappings(mappingForReturnModel);
      List<String> finaltagValueIds = columnTagValueAutoMappingModel.getTagValueIds();
      finaltagValueIds.addAll(tagValueIds);
      columnTagValueAutoMappingModel.setTagValueIds(finaltagValueIds);
    }
    return columnTagValueAutoMappingModel;
  }
  
  @SuppressWarnings("unchecked")
  private void getTagValuesMap(ITagModel tagModel, Map<String, Object> tagValueMap,
      List<String> tagValueIds, Set<String> ids, List<String> alreadyMapped)
  {
    List<String> duplicateTagValues = new ArrayList<>();
    List<ITag> tagValues = (List<ITag>) tagModel.getChildren();
    for (ITag tagValue : tagValues) {
      String label = tagValue.getLabel();
      String code = tagValue.getCode();
      String id = tagValue.getId();
      
      if (tagValueMap.get(id) == null && ids.contains(id)) {
        tagValueMap.put(id, tagValue.getId());
        tagValueIds.add(tagValue.getId());
      }
      // if (!alreadyMapped.contains(tagValue.getId())) {
      if (tagValueMap.get(code) == null && ids.contains(code)) {
        tagValueMap.put(code, tagValue.getId());
        tagValueIds.add(tagValue.getId());
      }
      if (!code.equals(label) && !id.equals(label)) {
        if (tagValueMap.get(label) == null && ids.contains(label)) {
          tagValueMap.put(label, tagValue.getId());
          tagValueIds.add(tagValue.getId());
        }
        else if (tagValueMap.get(label) != null) {
          duplicateTagValues.add(label);
        }
        // }
      }
    }
    tagValueMap.keySet()
        .removeAll(duplicateTagValues);
  }
  
  private List<ITagValueMappingModel> getAutoTagValueMapping(Set<String> ids,
      Map<String, Object> tagValueMap) throws RDBMSException, Exception
  {
    List<ITagValueMappingModel> tagValuesAutoMapping = new ArrayList<>();
    
    for (String id : ids) {
      ITagValueMappingModel tagValueMapping = new TagValueMappingModel();
      tagValueMapping.setTagValue(id);
      tagValueMapping.setMappedTagValueId("");
      tagValueMapping.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      if (tagValueMap.get(id) != null) {
        tagValueMapping.setMappedTagValueId((String) tagValueMap.get(id));
      }
      tagValuesAutoMapping.add(tagValueMapping);
    }
    return tagValuesAutoMapping;
  }
}
