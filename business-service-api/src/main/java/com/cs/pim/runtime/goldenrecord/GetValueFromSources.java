package com.cs.pim.runtime.goldenrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.model.goldenrecord.GetValueFromSourcesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetValueFromSourcesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetValueFromSourcesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IPropertyInfoModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class GetValueFromSources extends AbstractRuntimeService<IGetValueFromSourcesRequestModel, IGetValueFromSourcesResponseModel> implements IGetValueFromSources{

	@Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Autowired
  protected GoldenRecordUtils   goldenRecordUtils;
			
  @Override
  protected IGetValueFromSourcesResponseModel executeInternal(
      IGetValueFromSourcesRequestModel dataModel) throws Exception
  {
    IGoldenRecordDTO goldenRecordDTO = goldenRecordUtils.getGoldenRecordDTO(dataModel.getBucketId(),
        dataModel.getGoldenRecordId());
    List<Long> bucketBaseEntityIIDs = goldenRecordDTO.getLinkedBaseEntities();

    IPropertyInfoModel propertyInfo = dataModel.getPropertyInfo();
    String languageCode = dataModel.getLanguageCode();

    Map<String, IPropertyInstance> sourceIdValueMap = new HashMap<>();
    Set<String> sourceIds = new HashSet<>();
    
    IPropertyDTO property = ConfigurationDAO.instance().getPropertyByCode(propertyInfo.getPropertyId());
    
    Map<Long, List<IPropertyRecordDTO>> iidVsPropertyRecordDTO = goldenRecordUtils
        .getPropertyRecord(property, bucketBaseEntityIIDs.toArray(new Long[0]));
    
    Map<String, Map<String, List<IPropertyRecordDTO>>> baseEntityIIDVsPropertyVsPropertyRecords = goldenRecordUtils
        .getPropertyVsPropertyRecordList(iidVsPropertyRecordDTO);
    
    switch (propertyInfo.getType()) {
      case CommonConstants.ATTRIBUTE:
        getAttributeValuesFromSources(baseEntityIIDVsPropertyVsPropertyRecords, propertyInfo,
            sourceIds, sourceIdValueMap, languageCode);
        break;
      case CommonConstants.TAG:
        getTagValuesFromSources(baseEntityIIDVsPropertyVsPropertyRecords, propertyInfo, sourceIds,
            sourceIdValueMap);
        break;
    }
    
    fillDefaultResponseModel(propertyInfo, bucketBaseEntityIIDs, sourceIds, sourceIdValueMap);
    
    IGetValueFromSourcesResponseModel responseModel = new GetValueFromSourcesResponseModel();
    responseModel.setSourceIds(new ArrayList<String>(sourceIds));
    responseModel.setSourceIdValueMap(sourceIdValueMap);
    return responseModel;
  }
  
  private void getAttributeValuesFromSources(
      Map<String, Map<String, List<IPropertyRecordDTO>>> baseEntityIIDVsPropertyVsPropertyRecords,
      IPropertyInfoModel propertyInfo, Set<String> sourceIds,
      Map<String, IPropertyInstance> sourceIdValueMap, String languageCode) throws RDBMSException
  {
    baseEntityIIDVsPropertyVsPropertyRecords.forEach((sourceId, attributeVsValueRecordList) -> {

      IAttributeInstance attributeInstance = new AttributeInstance();
      sourceIds.add(sourceId);
      sourceIdValueMap.put(sourceId, attributeInstance);
      
      if (attributeVsValueRecordList != null) {
        List<IPropertyRecordDTO> valueRecordList = attributeVsValueRecordList.get(propertyInfo.getPropertyId());
        if (valueRecordList != null && !valueRecordList.isEmpty()) {
          if (propertyInfo.getIsTranslatable()) {
            for (IPropertyRecordDTO propertyRecord : valueRecordList) {
              IValueRecordDTO valueRecord = ((IValueRecordDTO) propertyRecord);
              if (valueRecord.getLocaleID().equals(languageCode)) {
                getAttributeInstance(attributeInstance, valueRecord.getProperty(), valueRecord, sourceId);
              }
            }
          }
          else {
            getAttributeInstance(attributeInstance, valueRecordList.get(0)
                .getProperty(), (IValueRecordDTO) valueRecordList.get(0), sourceId);
          }
        }
      }
    });
  }
  
  private void getTagValuesFromSources(
      Map<String, Map<String, List<IPropertyRecordDTO>>> baseEntityIIDVsPropertyVsPropertyRecords,
      IPropertyInfoModel propertyInfo, Set<String> sourceIds,
      Map<String, IPropertyInstance> sourceIdValueMap) throws RDBMSException
  {
    baseEntityIIDVsPropertyVsPropertyRecords.forEach((sourceId, tagVsTagRecordList) -> {
      
      ITagInstance tagInstance = new TagInstance();
      sourceIds.add(sourceId);
      sourceIdValueMap.put(sourceId, tagInstance);
      
      if (tagVsTagRecordList != null) {
        List<IPropertyRecordDTO> tagRecordList = tagVsTagRecordList.get(propertyInfo.getPropertyId());
        if (tagRecordList != null && !tagRecordList.isEmpty()) {
          getTagInstance(tagInstance, tagRecordList.get(0).getProperty(), (ITagsRecordDTO) tagRecordList.get(0), sourceId);
        }
      }
    });
  }
  
  private IAttributeInstance getAttributeInstance(IAttributeInstance attributeInstance,
      IPropertyDTO property, IValueRecordDTO valueRecordDTO, String sourceId)
  {
    attributeInstance.setAttributeId(property.getCode());
    attributeInstance.setBaseType(AttributeInstance.class.getName());
    attributeInstance.setCode(property.getCode());
    attributeInstance.setIid(property.getIID());
    attributeInstance.setValue(valueRecordDTO.getValue());
    attributeInstance.setValueAsHtml(valueRecordDTO.getAsHTML());
    attributeInstance.setValueAsNumber(valueRecordDTO.getAsNumber());
    attributeInstance.setKlassInstanceId(sourceId);
    
    return attributeInstance;
  }
  
  private ITagInstance getTagInstance(ITagInstance tagInstance, IPropertyDTO property,
      ITagsRecordDTO tagRecordDTO, String sourceId)
  {
    tagInstance.setBaseType(TagInstance.class.getName());
    tagInstance.setId(Long.toString(property.getIID()));
    tagInstance.setIid(property.getIID());
    tagInstance.setTagId(property.getCode());
    ITagInstanceValue tagValue = new TagInstanceValue();
    List<ITagInstanceValue> tagValueList = new ArrayList<ITagInstanceValue>();
    Set<ITagDTO> tags = tagRecordDTO.getTags();
    Iterator<ITagDTO> iterator = tags.iterator();
    while (iterator.hasNext()) {
      ITagDTO next = iterator.next();
      tagValue.setTagId(next.getTagValueCode());
      tagValue.setRelevance(next.getRange());
      tagValue.setCode(next.getTagValueCode());
      tagValue.setId(next.getTagValueCode());
      tagValueList.add(tagValue);
    }
    tagInstance.setTagValues(tagValueList);
    
    return tagInstance;
  }
  
  private void fillDefaultResponseModel(IPropertyInfoModel propertyInfo,
      List<Long> bucketBaseEntityIIDs, Set<String> sourceIds,
      Map<String, IPropertyInstance> sourceIdValueMap)
  {
    if (sourceIds.size() == bucketBaseEntityIIDs.size()) {
      return;
    }
    
    IPropertyInstance propertyInstance = new PropertyInstance();
    String propertyId = propertyInfo.getPropertyId();
    
    switch (propertyInfo.getType()) {
      
      case CommonConstants.ATTRIBUTE:
        IAttributeInstance attributeInstance = new AttributeInstance();
        attributeInstance.setAttributeId(propertyId);
        attributeInstance.setCode(propertyId);
        propertyInstance = attributeInstance;
        break;
        
      case CommonConstants.TAG:
        ITagInstance tagInstance = new TagInstance();
        tagInstance.setId(propertyId);
        tagInstance.setTagId(propertyId);
        propertyInstance = tagInstance;
        break;
    }
    
    for (Long baseEntityIID : bucketBaseEntityIIDs) {
      String sourceId = String.valueOf(baseEntityIID);
      if (!sourceIds.contains(sourceId)) {
        sourceIds.add(sourceId);
        sourceIdValueMap.put(sourceId, propertyInstance);
      }
    }
  }
}
