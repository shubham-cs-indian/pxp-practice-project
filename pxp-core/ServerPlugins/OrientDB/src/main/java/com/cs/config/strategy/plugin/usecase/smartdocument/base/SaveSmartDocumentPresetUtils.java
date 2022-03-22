package com.cs.config.strategy.plugin.usecase.smartdocument.base;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetEntityRule;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleTags;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetTagRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleEntityRule;
import com.cs.core.config.interactor.model.smartdocument.preset.IModifiedSmartDocumentPresetRuleEntityModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IModifiedSmartDocumentPresetTagRuleEntityModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveSmartDocumentPresetUtils {
  
  public static final List<String> fieldsToExclude = Arrays.asList(
      ISmartDocumentPresetModel.LAST_MODIFIED_BY, ISmartDocumentPresetModel.VERSION_ID,
      ISmartDocumentPresetModel.VERSION_TIMESTAMP, ISmartDocumentPresetModel.ATTRIBUTE_IDS,
      ISmartDocumentPresetModel.TAG_IDS, ISmartDocumentPresetModel.KLASS_IDS,
      ISmartDocumentPresetModel.TAXONOMY_IDS, ISmartDocumentPresetModel.ATTRIBUTES,
      ISmartDocumentPresetModel.TAGS);
  
  public static void manageFlatProperties(Vertex smartDocumentVertex,
      Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> smartDocumentPresetModel = (Map<String, Object>) requestMap
        .get(ISaveSmartDocumentPresetModel.SMART_DCOUMENT_PRESET);
    UtilClass.saveNode(smartDocumentPresetModel, smartDocumentVertex, fieldsToExclude);
  }
  
  public static void manageLinkedProperties(Vertex smartDocumentVertex,
      Map<String, Object> requestMap) throws Exception
  {
    // Attributes
    manageAddedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.ADDED_ATTRIBUTE_IDS),
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_ATTRIBUTE_LINK);
    manageDeletedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.DELETED_ATTRIBUTE_IDS),
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_ATTRIBUTE_LINK);
    
    // Tags
    manageAddedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.ADDED_TAG_IDS),
        VertexLabelConstants.ENTITY_TAG, RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAG_LINK);
    manageDeletedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.DELETED_TAG_IDS),
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAG_LINK);
    
    // Klasses
    manageAddedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.ADDED_KLASS_IDS),
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_KLASS_LINK);
    manageDeletedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.DELETED_KLASS_IDS),
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_KLASS_LINK);
    
    // Taxonomies
    manageAddedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.ADDED_TAXONOMY_IDS),
        VertexLabelConstants.ROOT_KLASS_TAXONOMY,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAXONOMY_LINK);
    manageDeletedProperties(smartDocumentVertex,
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.DELETED_TAXONOMY_IDS),
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAXONOMY_LINK);
  }
  
  private static void manageAddedProperties(Vertex smartDocumentVertex,
      List<String> addedPropertyIds, String propertyEntityType, String edgeLabel) throws Exception
  {
    if (addedPropertyIds.size() == 0) {
      return;
    }
    for (String addedProperty : addedPropertyIds) {
      Vertex propertyVertex = UtilClass.getVertexById(addedProperty, propertyEntityType);
      smartDocumentVertex.addEdge(edgeLabel, propertyVertex);
    }
  }
  
  private static void manageDeletedProperties(Vertex smartDocumentVertex,
      List<String> deletedPropertyIds, String edgeLabel) throws Exception
  {
    if (deletedPropertyIds.size() == 0) {
      return;
    }
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> availablePropertyEdges = smartDocumentVertex.getEdges(Direction.OUT, edgeLabel);
    for (Edge availablepropertyEdge : availablePropertyEdges) {
      Vertex propertyVertex = availablepropertyEdge.getVertex(Direction.IN);
      String propertyId = UtilClass.getCodeNew(propertyVertex);
      if (deletedPropertyIds.contains(propertyId)) {
        edgesToRemove.add(availablepropertyEdge);
        deletedPropertyIds.remove(propertyId);
      }
      if (deletedPropertyIds.isEmpty()) {
        break;
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }
  
  public static void handleAttributeRules(Vertex smartDocumentVertex,
      Map<String, Object> requestMap) throws Exception
  {
    SaveSmartDocumentPresetUtils.handleAddedAttributeRules(smartDocumentVertex,
        (List<Map<String, Object>>) requestMap
            .remove(ISaveSmartDocumentPresetModel.ADDED_ATTRIBUTE_RULES));
    SaveSmartDocumentPresetUtils.handleDeletedAttributeRules(
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.DELETED_ATTRIBUTE_RULES));
    SaveSmartDocumentPresetUtils.handleModifiedAttributeRules((List<Map<String, Object>>) requestMap
        .remove(ISaveSmartDocumentPresetModel.MODIFIED_ATTRIBUTE_RULES));
  }
  
  public static void handleAddedAttributeRules(Vertex smartDocumentPreset,
      List<Map<String, Object>> addedAttributeRules) throws Exception
  {
    OrientVertexType attributeIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET_ATTR_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> attributeRule : addedAttributeRules) {
      String entityId = (String) attributeRule
          .remove(ISmartDocumentPresetRuleIntermediateEntity.ENTITY_ID);
      
      List<Map<String, Object>> rules = (List<Map<String, Object>>) attributeRule
          .remove(ISmartDocumentPresetRuleIntermediateEntity.RULES);
      Vertex attributeRuleVertex = UtilClass.createNode(attributeRule,
          attributeIntermediateVertexType, new ArrayList<>());
      Vertex attribute = UtilClass.getVertexById(entityId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      attributeRuleVertex.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_ATTR_LINK,
          attribute);
      smartDocumentPreset.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_ATTRIBUTE,
          attributeRuleVertex);
      
      for (Map<String, Object> rule : rules) {
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType, new ArrayList<>());
        attributeRuleVertex.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK,
            ruleNode);
      }
    }
  }
  
  public static void handleDeletedAttributeRules(List<String> deletedAttributeRuleIds)
      throws Exception
  {
    for (String id : deletedAttributeRuleIds) {
      try {
        Vertex attributeIntermediate = UtilClass.getVertexById(id,
            VertexLabelConstants.SMART_DOCUMENT_PRESET_ATTR_INTERMEDIATE);
        deleteIntermediateWithRuleNodes(attributeIntermediate);
      }
      catch (NotFoundException e) {
        // node already deleted. Do nothing
      }
    }
  }
  
  public static void deleteIntermediateWithRuleNodes(Vertex intermediate)
  {
    Iterable<Vertex> rules = intermediate.getVertices(Direction.OUT,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK);
    UtilClass.deleteVertices(rules);
    intermediate.remove();
  }
  
  public static void handleModifiedAttributeRules(List<Map<String, Object>> modifiedAttributeRules)
      throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE, CommonConstants.CODE_PROPERTY);
    
    for (Map<String, Object> modifiedAttribute : modifiedAttributeRules) {
      
      String id = (String) modifiedAttribute.get(IModifiedSmartDocumentPresetRuleEntityModel.ID);
      Vertex attibuteIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.SMART_DOCUMENT_PRESET_ATTR_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedAttribute
          .get(IModifiedSmartDocumentPresetRuleEntityModel.ADDED_RULES);
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedAttribute
          .get(IModifiedSmartDocumentPresetRuleEntityModel.MODIFIED_RULES);
      List<String> deletedRules = (List<String>) modifiedAttribute
          .get(IModifiedSmartDocumentPresetRuleEntityModel.DELETED_RULES);
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, new ArrayList<>());
        attibuteIntermediate.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK,
            ruleNode);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule
            .get(IModifiedSmartDocumentPresetRuleEntityModel.ID);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId,
            VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE);
        List<String> values = (List<String>) modifiedRule
            .get(ISmartDocumentPresetEntityRule.VALUES);
        Boolean shouldCompareWithSystemDate = (Boolean) modifiedRule
            .get(IDataRuleEntityRule.SHOULD_COMPARE_WITH_SYSTEM_DATE);
        ruleNode.setProperty(ISmartDocumentPresetEntityRule.TO,
            (String) modifiedRule.get(ISmartDocumentPresetEntityRule.TO));
        ruleNode.setProperty(ISmartDocumentPresetEntityRule.FROM,
            (String) modifiedRule.get(ISmartDocumentPresetEntityRule.FROM));
        ruleNode.setProperty(ISmartDocumentPresetEntityRule.TYPE,
            (String) modifiedRule.get(ISmartDocumentPresetEntityRule.TYPE));
        ruleNode.setProperty(ISmartDocumentPresetEntityRule.VALUES, values);
        if (shouldCompareWithSystemDate != null) {
          ruleNode.setProperty(ISmartDocumentPresetEntityRule.SHOULD_COMPARE_WITH_SYSTEM_DATE,
              shouldCompareWithSystemDate);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId,
            VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleTagRules(Vertex smartDocumentVertex, Map<String, Object> requestMap)
      throws Exception
  {
    SaveSmartDocumentPresetUtils.handleAddedTagRules(smartDocumentVertex,
        (List<Map<String, Object>>) requestMap
            .remove(ISaveSmartDocumentPresetModel.ADDED_TAG_RULES));
    SaveSmartDocumentPresetUtils.handleDeletedTagRules(
        (List<String>) requestMap.remove(ISaveSmartDocumentPresetModel.DELETED_TAG_RULES));
    SaveSmartDocumentPresetUtils.handleModifiedTagRules((List<Map<String, Object>>) requestMap
        .remove(ISaveSmartDocumentPresetModel.MODIFIED_TAG_RULES));
  }
  
  public static void handleAddedTagRules(Vertex smartDocumentPreset,
      List<Map<String, Object>> addedTagRules) throws Exception
  {
    OrientVertexType tagIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET_TAG_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE, CommonConstants.CODE_PROPERTY);
    
    for (Map<String, Object> tagRule : addedTagRules) {
      String entityId = (String) tagRule.remove(ISmartDocumentPresetRuleTags.ENTITY_ID);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) tagRule.remove(ISmartDocumentPresetRuleTags.RULES);
      Vertex tagRuleVertex = UtilClass.createNode(tagRule, tagIntermediateVertexType,
          new ArrayList<>());
      Vertex tag = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      tagRuleVertex.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_LINK, tag);
      smartDocumentPreset.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG,
          tagRuleVertex);
      for (Map<String, Object> rule : rules) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) rule
            .remove(ISmartDocumentPresetTagRule.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType, new ArrayList<>());
        tagRuleVertex.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK, ruleNode);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(IDataRuleTagValues.TO);
          int from = (int) tagValue.get(IDataRuleTagValues.FROM);
          String id = (String) tagValue.get(IDataRuleTagValues.ID);
          Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          ruleNode.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK,
              tagValueNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(IDataRuleTagValues.TO, to);
          propertyMap.put(IDataRuleTagValues.FROM, from);
          idPropertyMap.put(id, propertyMap);
        }
        setTagValueEdgeProperty(idPropertyMap, ruleNode,
            RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK);
      }
    }
  }
  
  private static void setTagValueEdgeProperty(Map<String, Map<String, Object>> idPropertyMap,
      Vertex node, String edgeLabel)
  {
    Iterable<Edge> edges = node.getEdges(Direction.OUT, edgeLabel);
    for (Edge edge : edges) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      String tagId = tagValueNode.getProperty(CommonConstants.CODE_PROPERTY);
      Map<String, Object> propertyMap = (Map<String, Object>) idPropertyMap.get(tagId);
      edge.setProperty(IDataRuleTagValues.TO, (int) propertyMap.get(IDataRuleTagValues.TO));
      edge.setProperty(IDataRuleTagValues.FROM, (int) propertyMap.get(IDataRuleTagValues.FROM));
      edge.setProperty(IDataRuleTagValues.INNER_TAG_ID, tagId);
    }
  }
  
  public static void handleDeletedTagRules(List<String> deletedTagRuleIds) throws Exception
  {
    for (String id : deletedTagRuleIds) {
      Vertex tagIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.SMART_DOCUMENT_PRESET_TAG_INTERMEDIATE);
      deleteIntermediateWithRuleNodes(tagIntermediate);
    }
  }
  
  public static void handleModifiedTagRules(List<Map<String, Object>> modifiedTagRules)
      throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedTagRule : modifiedTagRules) {
      String modifiedTagRuleId = (String) modifiedTagRule
          .get(IModifiedSmartDocumentPresetTagRuleEntityModel.ID);
      Vertex tagIntermediate = UtilClass.getVertexById(modifiedTagRuleId,
          VertexLabelConstants.SMART_DOCUMENT_PRESET_TAG_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedTagRule
          .get(IModifiedSmartDocumentPresetTagRuleEntityModel.ADDED_RULES);
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedTagRule
          .get(IModifiedSmartDocumentPresetTagRuleEntityModel.MODIFIED_RULES);
      List<String> deletedRules = (List<String>) modifiedTagRule
          .get(IModifiedSmartDocumentPresetTagRuleEntityModel.DELETED_RULES);
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) addedRule
            .remove(ISmartDocumentPresetTagRule.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, new ArrayList<>());
        tagIntermediate.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK,
            ruleNode);
        for (Map<String, Object> tagValue : tagValues) {
          String id = (String) tagValue.get(IDataRuleTagValues.ID);
          Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          ruleNode.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK,
              tagValueNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(IDataRuleTagValues.TO, (int) tagValue.get(IDataRuleTagValues.TO));
          propertyMap.put(IDataRuleTagValues.FROM, (int) tagValue.get(IDataRuleTagValues.FROM));
          idPropertyMap.put(id, propertyMap);
        }
        setTagValueEdgeProperty(idPropertyMap, ruleNode,
            RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(ISmartDocumentPresetTagRule.ID);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId,
            VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE);
        String oldType = ruleNode.getProperty(ISmartDocumentPresetTagRule.TYPE);
        String newType = (String) modifiedRule.get(ISmartDocumentPresetTagRule.TYPE);
        ruleNode.setProperty(ISmartDocumentPresetTagRule.TYPE, newType);
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) modifiedRule
            .get(ISmartDocumentPresetTagRule.TAG_VALUES);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(IDataRuleTagValues.TO);
          int from = (int) tagValue.get(IDataRuleTagValues.FROM);
          String id = (String) tagValue.get(IDataRuleTagValues.ID);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(IDataRuleTagValues.TO, to);
          propertyMap.put(IDataRuleTagValues.FROM, from);
          idPropertyMap.put(id, propertyMap);
        }
        if (oldType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            || oldType.equals(CommonConstants.EMPTY_PROPERTY)) {
          if (!newType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
              && !newType.equals(CommonConstants.EMPTY_PROPERTY)) {
            createRuleTagValueEdges(ruleNode, tagValues);
          }
        }
        if (!newType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            && !newType.equals(CommonConstants.EMPTY_PROPERTY)) {
          setTagValueEdgeProperty(idPropertyMap, ruleNode,
              RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId,
            VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE);
        ruleNode.remove();
      }
    }
  }
  
  public static void createRuleTagValueEdges(Vertex ruleNode, List<Map<String, Object>> tagValues)
      throws Exception
  {
    
    Iterable<Edge> edges = ruleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK);
    if (!(edges.iterator()
        .hasNext())) {
      for (Map<String, Object> tagValue : tagValues) {
        String id = (String) tagValue.get(IDataRuleTagValues.ID);
        Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        ruleNode.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK,
            tagValueNode);
      }
    }
  }
}
