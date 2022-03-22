package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.validationontype.InvalidTagTypeException;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.tag.ICreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateTag extends AbstractOrientPlugin {
  
  public static List<String> fieldsToExclude = Arrays.asList(ITagModel.CHILDREN,
      ITagModel.DEFAULT_VALUE, ITagModel.TAG_VALUES, ITagModel.ALLOWED_TAGS, ITagModel.PARENT);
  
  public CreateTag(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> tagMap = (Map<String, Object>) requestMap.get(CommonConstants.TAG);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    List<Map<String,Object>> returnList = new ArrayList();

    if (!ValidationUtils.validateTagInfo(tagMap)) {
      return returnMap;
    }
    
    HashMap<String, Object> parentTagMap = (HashMap<String, Object>) tagMap.get("parent");
    String vertexLabel = VertexLabelConstants.ENTITY_TAG;
    Vertex parentTagNode = null;
    if (parentTagMap != null) {
      String parentId = (String) parentTagMap.get(CommonConstants.ID_PROPERTY);
      try {
        parentTagNode = UtilClass.getVertexByIndexedId(parentId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      String tagType = parentTagNode.getProperty(ITag.TAG_TYPE);
      if (tagType.equals(SystemLevelIds.MASTER_TAG_TYPE_ID)) {
        vertexLabel = VertexLabelConstants.ATTRIBUTION_TAXONOMY;
        tagMap.put(IMasterTaxonomy.IS_TAG, true);
        tagMap.remove(IMasterTaxonomy.TYPE);
      }
      else if (tagType.equals(SystemLevelIds.LANGUAGE_TAG_TYPE_ID)) {
        vertexLabel = VertexLabelConstants.LANGUAGE;
        tagMap.put(IMasterTaxonomy.IS_TAG, true);
        tagMap.remove(IMasterTaxonomy.TYPE);
      }
    }
    else {
      try {
        UtilClass.validateOnType(IStandardConfig.TagType.AllTagTypes,
            (String) tagMap.get(ITag.TAG_TYPE), false);
        tagMap.put(ITag.IS_ROOT, true);
      }
      catch (InvalidTypeException e) {
        throw new InvalidTagTypeException(e);
      }
    }
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(vertexLabel,
        CommonConstants.CODE_PROPERTY);
    Vertex tagNode = UtilClass.createNode(tagMap, vertexType, fieldsToExclude);
    HashMap<String, Object> defaultTagMap = (HashMap<String, Object>) tagMap
        .get(ITagModel.DEFAULT_VALUE);
    if (defaultTagMap != null) {
      String tagId = (String) defaultTagMap.get("id");
      if (tagId != null) {
        Vertex defaultTagValue = UtilClass.getVertexByIndexedId(tagId,
            VertexLabelConstants.ENTITY_TAG);
        defaultTagValue.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF,
            tagNode);
      }
    }
    
    TagUtils.createAndlinkTagAndTagType(tagMap, tagNode);
    String tagType = (String) tagMap.get(ITagModel.TAG_TYPE);
    Boolean isParentExist =  parentTagNode != null;
    if (isParentExist) {
      tagNode.removeProperty(ITagModel.TAG_TYPE);
      tagNode.removeProperty(ITagModel.TAG_VALUES_SEQUENCE);
      
      TagUtils.linkTagNodeToNormalizationAndRuleNodes(tagNode, parentTagNode);
      tagNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentTagNode);
      // major change to remove "parent" property from tag
      tagNode.removeProperty(ITagModel.PARENT);
      
      // for adding the childValue id to the tagGroup sequence
      List<String> addSequenceTagChildValue = parentTagNode
          .getProperty(ITagModel.TAG_VALUES_SEQUENCE);
      addSequenceTagChildValue.add((String) tagMap.get(IConfigModel.CODE));
      parentTagNode.setProperty(ITagModel.TAG_VALUES_SEQUENCE, addSequenceTagChildValue);
    }
    else if (tagType.equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
      TagUtils.createDefaultBooleanChild(tagNode);
    }
    else if (tagType.equals(SystemLevelIds.MASTER_TAG_TYPE_ID)) {
      tagNode.setProperty(ITagModel.IS_MULTI_SELECT, true);
    }
    
    String linkedMasterTagId = (String) tagMap.get(ITaxonomy.LINKED_MASTER_TAG_ID);
    if (linkedMasterTagId != null && !linkedMasterTagId.isEmpty()) {
      Vertex masterTagNode = UtilClass.getVertexById(linkedMasterTagId,
          VertexLabelConstants.ENTITY_TAG);
      tagNode.setProperty(EntityUtil.getLanguageConvertedField(ITagModel.LABEL),
          masterTagNode.getProperty(ITag.LABEL));
      masterTagNode.addEdge(RelationshipLabelConstants.MASTER_TAG_OF, tagNode);
    }
    
    Vertex tagVertexForAuditInfo = isParentExist ? parentTagNode : tagNode;

    Map<String, Object> auditLogInfoModelMap = AuditLogUtils.prepareAndGetAuditLogInfoModel(
        tagVertexForAuditInfo, Entities.PROPERTIES, Elements.TAGS,
        (String) tagVertexForAuditInfo
            .getProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)));
    if(isParentExist) {
      auditLogInfoModelMap.put(IAuditLogModel.ACTIVITY, ServiceType.UPDATED);
    }
    auditInfoList.add(auditLogInfoModelMap);
    UtilClass.getGraph()
        .commit();
    Map<String, Object> tagResponseMap = TagUtils.getTagMap(tagNode, false);
    returnList.add(tagResponseMap);
    returnMap.put(ICreateTagResponseModel.AUDIT_LOG_INFO, auditInfoList); 
    returnMap.put(ICreateTagResponseModel.CREATE_TAG_RESPONSE, returnList);

    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateTag/*" };
  }
}
