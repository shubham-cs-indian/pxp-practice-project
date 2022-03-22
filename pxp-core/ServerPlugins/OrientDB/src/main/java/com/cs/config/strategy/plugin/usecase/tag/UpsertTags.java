package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.tag.BulkSaveTagFailedException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author tauseef
 */
public class UpsertTags extends AbstractUpdateTag {

  public UpsertTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override protected Object execute(Map<String, Object> requestMap) throws Exception {
    List<Map<String, Object>> listOfTags = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> tagList = prepareTagList(listOfTags);
    List<Map<String, Object>> listOfSuccessSaveTag = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> booleanTags = new ArrayList<>();
    
    upsertTag(tagList, null, listOfSuccessSaveTag, failure, booleanTags);

    if (listOfSuccessSaveTag.isEmpty()) {
      throw new BulkSaveTagFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }

    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveTagResponse = new HashMap<String, Object>();
    Map<String, Object> successMap = new HashMap<>();

    List<String> sucessTagCodeList = listOfSuccessSaveTag.stream().map(t-> (String)t.get(CommonConstants.CODE_PROPERTY)).collect(Collectors.toList());
    listOfSuccessSaveTag.forEach(t -> t.get(CommonConstants.CODE_PROPERTY));
    successMap.put(IGetTagGridResponseModel.TAGS_LIST, sucessTagCodeList);

    bulkSaveTagResponse.put(IBulkSaveTagResponseModel.SUCCESS, successMap);
    bulkSaveTagResponse.put(IBulkSaveTagResponseModel.FAILURE, failure);
    bulkSaveTagResponse.put(SystemLevelIds.BOOLEAN_TAG_TYPE_ID, booleanTags);

    return bulkSaveTagResponse;
  }

  private List<Map<String, Object>> prepareTagList(List<Map<String, Object>> listOfTags)
  {
    List<Map<String, Object>> masterTag = new ArrayList<>();
    List<Map<String, Object>> tagList = new ArrayList<>();
    String tagType = ConfigTag.tagType.name();
    for(Map<String,Object> tagMap : listOfTags) {
      String tagTypeValue = (String) tagMap.get(tagType);
      tagMap.put(ITagModel.IS_ROOT, true);
      if(CommonConstants.MASTER_TAG_TYPE_ID.equals(tagTypeValue)) {
        masterTag.add(tagMap);
      }else {
        tagList.add(tagMap);
      }
    }
    
    masterTag.addAll(tagList);
    return masterTag;
  }

  private void upsertTag(List<Map<String, Object>> listOfTags, String parentCode,
      List<Map<String, Object>> listOfSuccessSaveTag, IExceptionModel failure, List<Map<String, Object>> booleanTags) {
    for (Map<String, Object> tagMap : listOfTags) {
      try {
        String code = (String) tagMap.get(ITag.CODE);
        String tagType = (String) tagMap.get(ITag.TAG_TYPE);
        String masterTagId = (String) tagMap.get(ITag.LINKED_MASTER_TAG_ID);
        List<Map<String, Object>> children = (List<Map<String, Object>>) tagMap.get(ITag.CHILDREN);
        tagMap.remove(ITag.CHILDREN);

        //If parent is already there then ignore otherwise evaluate parentId
        if(tagMap.get(ITag.PARENT) == null && !StringUtils.isEmpty(parentCode)) {
          Map<String, Object> parentInfo = new HashMap<>();
          parentInfo.put(ITag.ID, parentCode);
          tagMap.put(ITag.PARENT, parentInfo);
        }
        
        //handle defaultVlue create and update
        String defaultValue = (String) tagMap.get(CommonConstants.DEFAULT_VALUE_PROPERTY);
        Map<String, Object> defaultValueMap = new HashMap<>();
        if(defaultValue != null) {
          defaultValueMap.put(ITag.ID, defaultValue);
          tagMap.remove(CommonConstants.DEFAULT_VALUE_PROPERTY);
        }
        
        try {
          tagMap = updateTag(tagMap, new ArrayList<>());
        }
        catch (TagNotFoundException e) {
          tagMap = createTag(tagMap, failure);
        }
        
        // for adding the childValue id to the tagGroup sequence
        if(StringUtils.isNoneEmpty(parentCode)) {
          Vertex parentTagNode = UtilClass.getVertexByCode(parentCode, VertexLabelConstants.ENTITY_TAG);
          List<String> addSequenceTagChildValue = parentTagNode.getProperty(ITagModel.TAG_VALUES_SEQUENCE);
          if(addSequenceTagChildValue == null)
            addSequenceTagChildValue = new ArrayList<>();
          addSequenceTagChildValue.add(code);
          parentTagNode.setProperty(ITagModel.TAG_VALUES_SEQUENCE, addSequenceTagChildValue);
        }
        
        Vertex tagNode = UtilClass.getVertexByCode(code, VertexLabelConstants.ENTITY_TAG);
        if (!CommonConstants.MASTER_TAG_TYPE_ID.equals(tagType)) {
          tagMap.put(ITag.LINKED_MASTER_TAG_ID, masterTagId);
          updateLinkedMasterTag(tagMap, tagNode);
        }

        listOfSuccessSaveTag.add(tagMap);
        boolean isParent = false;
        if(children != null && children.size() > 0) {
          isParent = true;
          children.forEach(c -> c.put(ITag.TAG_TYPE, tagType));
          upsertTag(children, code, listOfSuccessSaveTag, failure, booleanTags);
        }
        
        //Add or remove default tagValue
        if (isParent) {
          tagMap.put(ITag.DEFAULT_VALUE, defaultValueMap);
          updateDefaultTagValue(tagMap, tagNode);
        }
        
        // For boolean tag
        if (tagType.equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
          TagUtils.createDefaultBooleanChild(tagNode);
          booleanTags.add(TagUtils.getTagMap(tagNode, false));
        }
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
    }
  }

  private Map<String, Object> createTag(Map<String, Object> tagMap, IExceptionModel failure) {
    String code = (String) tagMap.get(IAttribute.CODE);
    String label = (String) tagMap.get(IAttribute.LABEL);

    if(tagMap.get(ITag.PARENT) == null) {
      tagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
    }

    try {
      return TagUtils.createTag(tagMap, new ArrayList<>());
    } catch (Exception ex) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, code, label);
    }
    return tagMap;
  }

  @Override public String[] getNames() {
    return new String[] { "POST|UpsertTags/*" };
  }
}
