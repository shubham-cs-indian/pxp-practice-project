package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.goldenrecord.GoldenRecordRuleUtil;
import com.cs.config.strategy.plugin.usecase.governancerule.GovernanceRuleUtil;
import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.exception.tag.AttributionTaxonomyTagCanNotBeDeleted;
import com.cs.core.config.interactor.exception.tag.TagLinkedToTaxonomyHierarchyCannotBeDeletedException;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class DeleteTags extends AbstractOrientPlugin {
  
  public DeleteTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> idsToDelete = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    
    List<String> deletedIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    for (String id : idsToDelete) {
      
      try {
        Vertex tagNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TAG);
        if(ValidationUtils.vaildateIfStandardEntity(tagNode))
          continue;
        if (tagNode != null) {
          Iterator<Edge> levelEdges = tagNode
              .getEdges(Direction.OUT, RelationshipLabelConstants.LEVEL_TAGGROUP_OF)
              .iterator();
          
          Boolean isAttributionTaxonomy = tagNode
              .getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
              .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
          Boolean isLanguageTaxonomy = tagNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
              .equals(VertexLabelConstants.LANGUAGE);
          
          Boolean isTaxonomy = tagNode.getProperty(IMasterTaxonomy.IS_TAXONOMY);
          isTaxonomy = isTaxonomy == null ? false : isTaxonomy;
          if (((isAttributionTaxonomy || isLanguageTaxonomy) && isTaxonomy)
              || levelEdges.hasNext()) {
            throw new AttributionTaxonomyTagCanNotBeDeleted();
          }
          
          Iterator<Edge> taxonomyLevelEdge = tagNode
              .getEdges(Direction.IN, RelationshipLabelConstants.TAXONOMY_LEVEL)
              .iterator();
          Iterator<Edge> hasMasterTagEdge = tagNode
              .getEdges(Direction.IN, RelationshipLabelConstants.HAS_MASTER_TAG)
              .iterator();
          if (taxonomyLevelEdge.hasNext() || hasMasterTagEdge.hasNext()) {
            throw new TagLinkedToTaxonomyHierarchyCannotBeDeletedException();
          }
          
          Vertex parentTagNode = TagUtils.getParentTag(tagNode);
          Boolean isParentExist = parentTagNode != null;
          if (isParentExist) {
            // if node is a tag value
            
            String countQuery = "select count(*) from "
                + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + " where in = "
                + parentTagNode.getId();
            Long childTagCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
            
            if (childTagCount == 1) {
              DataRuleUtils.deleteRuleNodesLinkedToEntityNode(tagNode,
                  RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
              DataRuleUtils.deleteVerticesWithInDirection(tagNode,
                  RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
              GovernanceRuleUtil.deleteRuleNodesLinkedToEntityNode(tagNode,
                  RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK);
            }
          }
          
          Iterator<Edge> i = tagNode
              .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_TYPE_OF)
              .iterator();
          Edge tagTypeOfRelationship = null;
          while (i.hasNext()) {
            tagTypeOfRelationship = i.next();
          }
          
          if (tagTypeOfRelationship != null) {
            // if node is a parent tag
            DataRuleUtils.deleteVerticesWithInDirection(tagNode,
                RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
            DataRuleUtils.deleteIntermediateVerticesWithInDirection(tagNode,
                RelationshipLabelConstants.TAG_DATA_RULE_LINK);
            DataRuleUtils.deleteVerticesWithInDirection(tagNode,
                RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
            
            GovernanceRuleUtil.deleteIntermediateVerticesWithInDirection(tagNode,
                RelationshipLabelConstants.GOVERNANCE_RULE_TAG_LINK);
            GovernanceRuleUtil.deleteKPITag(tagNode);
            
            Vertex tagTypeNode = tagTypeOfRelationship.getVertex(Direction.OUT);
            tagTypeOfRelationship.remove();
            String existingTagTypeId = (String) tagTypeNode
                .getProperty(CommonConstants.CODE_PROPERTY);
            if (existingTagTypeId.equals(CommonConstants.CUSTOM_TAG_TYPE_ID)) {
              
              Iterable<Edge> tagValueRelationships = tagNode.getEdges(Direction.IN,
                  RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF);
              
              for (Edge tagValueRelationship : tagValueRelationships) {
                
                Vertex tagValueNodeToDelete = tagValueRelationship.getVertex(Direction.OUT);
                
                tagValueRelationship.remove();
                tagValueNodeToDelete.remove();
              }
            }
          }
          
          Iterable<Edge> allowedTagsRelationship = tagNode.getEdges(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_ALLOWED_TAGS);
          
          for (Edge allowedTagRelationship : allowedTagsRelationship) {
            allowedTagRelationship.remove();
          }
          
          Iterable<Vertex> children = tagNode.getVertices(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
          for (Vertex child : children) {
            deleteTaxonomyNodesAttached(child);
            child.remove();
          }
          // for removing the childNode id from sequence of ParentTag
          if (isParentExist) {
            List<String> sequenceProperty = parentTagNode
                .getProperty(ITagModel.TAG_VALUES_SEQUENCE);
            sequenceProperty.remove(UtilClass.getCodeNew(tagNode));
            parentTagNode.setProperty(ITagModel.TAG_VALUES_SEQUENCE, sequenceProperty);
          }
          else {
            // update the property collection
            Iterable<Vertex> propertyCollections = tagNode.getVertices(Direction.OUT,
                RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
            for (Vertex propertyCollection : propertyCollections) {
              List<String> propertyCollectionTagIds = (List<String>) propertyCollection
                  .getProperty(IPropertyCollection.TAG_IDS);
              propertyCollectionTagIds.remove(id);
            }
          }
          
          GridEditUtil.removePropertyFromGridEditSequenceList(
              tagNode.getProperty(CommonConstants.CODE_PROPERTY));
          deleteContextNodesAttached(tagNode);
          deleteSectionElementNodesAttached(tagNode);
          deleteTaxonomyNodesAttached(tagNode);
          deletePermissionNodesAttached(tagNode);
          deleteConcatenatedNodeAttached(tagNode);
          deleteTemplatePermissionNodesByPropertyNode(tagNode);
          GoldenRecordRuleUtil.deleteGoldenRecordMergeEffectTypeNode(tagNode);
       
          Vertex tagVertexForAuditInfo = isParentExist ? parentTagNode : tagNode;
          Map<String, Object> auditLogInfoModelMap = AuditLogUtils.prepareAndGetAuditLogInfoModel(
              tagVertexForAuditInfo, Entities.PROPERTIES, Elements.TAGS,
              (String) tagVertexForAuditInfo.getProperty(
                  EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)));
          if(isParentExist) {
            auditLogInfoModelMap.put(IAuditLogModel.ACTIVITY, ServiceType.UPDATED);
          }
          auditInfoList.add(auditLogInfoModelMap);
          
          tagNode.remove();
        }
        deletedIds.add(id);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> response = new HashMap<>();
    response.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    response.put(IBulkDeleteReturnModel.FAILURE, failure);
    response.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditInfoList); 

    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteTags/*" };
  }
  
  public void deleteSectionElementNodesAttached(Vertex tagNode)
  {
    Iterator<Edge> iterator = tagNode
        .getEdges(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    while (iterator.hasNext()) {
      Edge hasProperty = iterator.next();
      Vertex klassPropertyNode = hasProperty.getVertex(Direction.OUT);
      KlassUtils.removeLinkbetweenSectionElementAndNotificationSetting(klassPropertyNode);
      // KlassUtils.removeAttachedSectionElementPermissionNodes(klassPropertyNode);
      klassPropertyNode.remove();
    }
  }
  
  public void deleteTaxonomyNodesAttached(Vertex tagNode)
  {
    Iterable<Vertex> defaultFilterVertices = tagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.DEFAULT_FILTER_TAG_LINK);
    
    for (Vertex vertex : defaultFilterVertices) {
      vertex.remove(); // delete the default filter linked to tag group
    }
    
    Iterable<Vertex> filterableTagvertices = tagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.TAG_FILTERABLE_TAG_LINK);
    
    for (Vertex vertex : filterableTagvertices) {
      vertex.remove(); // delete the filterable tag vertex linked to tag group
    }
  }
  
  public void deleteContextNodesAttached(Vertex tagNode)
  {
    Iterable<Vertex> defaultFilterVertices = tagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY);
    
    for (Vertex vertex : defaultFilterVertices) {
      vertex.remove(); // delete the default context linked to tag group
    }
  }
  
  private void deletePermissionNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> iterable = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Vertex permissionNode : iterable) {
      permissionNode.remove();
    }
  }
  
  /**
   * @author Lokesh
   * @param propertyNode
   */
  private void deleteTemplatePermissionNodesByPropertyNode(Vertex propertyNode)
  {
    Iterable<Vertex> permissionIterable = propertyNode.getVertices(Direction.IN,
        RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  private void deleteConcatenatedNodeAttached(Vertex tagNode)
  {
    Iterable<Vertex> iterable = tagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK);
    for (Vertex concatNode : iterable) {
      concatNode.remove();
    }
  }
}
