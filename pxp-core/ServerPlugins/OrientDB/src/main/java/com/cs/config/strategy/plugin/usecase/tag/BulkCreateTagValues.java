package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.tag.BulkCreateTagValuesFailedException;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkCreateTagValuesSuccessModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class BulkCreateTagValues extends AbstractOrientPlugin {
  
  public BulkCreateTagValues(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> successCreatedTags = new ArrayList<>();
    List<Map<String, Object>> listOfChildTagsToCreate = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    Map<String, Object> referencedTags = new HashMap<>();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    for (Map<String, Object> childTag : listOfChildTagsToCreate) {
      try {
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ENTITY_TAG, CommonConstants.CODE_PROPERTY);
        List<String> fieldsToExclude = new ArrayList<>(Arrays.asList(ITagModel.PARENT,
            ITagModel.DEFAULT_VALUE, ITagModel.TAG_VALUES, ITagModel.LINKED_MASTER_TAG_ID));
        Vertex childNode = UtilClass.createNode(childTag, vertexType, fieldsToExclude);
        
        HashMap<String, Object> parentTagMap = (HashMap<String, Object>) childTag
            .get(ITagModel.PARENT);
        if (parentTagMap != null) {
          childNode.removeProperty(ITag.TAG_TYPE);
          String parentId = (String) parentTagMap.get(CommonConstants.ID_PROPERTY);
          Vertex parentNode = UtilClass.getVertexById(parentId, VertexLabelConstants.ENTITY_TAG);
          TagUtils.linkTagNodeToNormalizationAndRuleNodes(childNode, parentNode);
          childNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentNode);
          List<String> addSequenceTagChildValue = parentNode
              .getProperty(ITagModel.TAG_VALUES_SEQUENCE);
          addSequenceTagChildValue.add((String) childTag.get(IConfigModel.CODE));
          parentNode.setProperty(ITagModel.TAG_VALUES_SEQUENCE, addSequenceTagChildValue);
        }
        
        String linkedMasterTagId = (String) childTag.get(ITagModel.LINKED_MASTER_TAG_ID);
        if (linkedMasterTagId != null && !linkedMasterTagId.isEmpty()) {
          Vertex masterTagNode = UtilClass.getVertexById(linkedMasterTagId,
              VertexLabelConstants.ENTITY_TAG);
          childNode.setProperty(EntityUtil.getLanguageConvertedField(ITagModel.LABEL),
              UtilClass.getValueByLanguage(masterTagNode, ITagModel.LABEL));
          Map<String, Object> referencedTag = new HashMap<>();
          referencedTag = UtilClass
              .getMapFromVertex(
                  Arrays.asList(IConfigEntityInformationModel.ID,
                      IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.LABEL),
                  masterTagNode);
          referencedTags.put(linkedMasterTagId, referencedTag);
          masterTagNode.addEdge(RelationshipLabelConstants.MASTER_TAG_OF, childNode);
        }
        Vertex tagMapForAuditInfo = parentTagMap != null
            ? UtilClass.getVertexById((String) parentTagMap.get(CommonConstants.ID_PROPERTY),
                VertexLabelConstants.ENTITY_TAG)
            : UtilClass.getVertexById((String) childTag.get(CommonConstants.ID_PROPERTY),
                VertexLabelConstants.ENTITY_TAG);
            
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, tagMapForAuditInfo, Entities.PROPERTIES, Elements.TAGS); 
        successCreatedTags.add(TagUtils.getTagMap(childNode, true));
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
    }
    if (successCreatedTags.isEmpty()) {
      throw new BulkCreateTagValuesFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IBulkCreateTagValuesSuccessModel.REFERENCED_TAGS, referencedTags);
    successMap.put(IBulkCreateTagValuesSuccessModel.TAGS, successCreatedTags);
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, successMap);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditLogInfoList);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateTagValues/*" };
  }
}
