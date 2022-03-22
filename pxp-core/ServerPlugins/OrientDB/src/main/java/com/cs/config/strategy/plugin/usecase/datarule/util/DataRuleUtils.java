package com.cs.config.strategy.plugin.usecase.datarule.util;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeValueNormalization;
import com.cs.core.config.interactor.entity.datarule.IConcatenatedNormalization;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.datarule.IDataRuleEntityRule;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import java.util.*;

@SuppressWarnings("unchecked")
public class DataRuleUtils {
  
  public static void deleteIntermediateWithRuleNodes(Vertex intermediate)
  {
    Iterable<Vertex> rules = intermediate.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_LINK);
    UtilClass.deleteVertices(rules);
    intermediate.remove();
  }
  
  public static void deleteRuleNodesLinkedToEntityNode(Vertex entity, String edgeLabel)
  {
    Iterable<Vertex> nodesToDelete = entity.getVertices(Direction.IN, edgeLabel);
    for (Vertex nodeToDelete : nodesToDelete) {
      deleteRuleNode(nodeToDelete);
    }
  }
  
  public static void deleteVerticesWithInDirection(Vertex node, String edgeLabel)
  {
    Iterable<Vertex> nodesToDelete = node.getVertices(Direction.IN, edgeLabel);
    for (Vertex nodeToDelete : nodesToDelete) {
      nodeToDelete.remove();
    }
  }
  
  public static void deleteIntermediateVerticesWithInDirection(Vertex node, String edgeLabel)
  {
    Iterable<Vertex> nodesToDelete = node.getVertices(Direction.IN, edgeLabel);
    for (Vertex nodeToDelete : nodesToDelete) {
      deleteIntermediateWithRuleNodes(nodeToDelete);
    }
  }
  
  public static void deleteRuleNode(Vertex ruleNodeToBeDeleted)
  {
    Iterable<Vertex> intermediateVertices = ruleNodeToBeDeleted.getVertices(Direction.IN,
        RelationshipLabelConstants.RULE_LINK);
    for (Vertex intermediateVertex : intermediateVertices) {
      int ruleNodeCount = 0;
      Iterable<Vertex> ruleNodes = intermediateVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex ruleNode : ruleNodes) {
        ruleNodeCount++;
      }
      if (ruleNodeCount == 1) {
        // delete intermediate vertex if only current rule node is attached
        intermediateVertex.remove();
      }
    }
    ruleNodeToBeDeleted.remove();
  }
  
  public static void addAttributeRules(Vertex dataRule, List<Map<String, Object>> attributeRules)
      throws Exception
  {
    OrientVertexType attributeIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ATTRIBUTE_RULE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> attributeRule : attributeRules) {
      String entityId = (String) attributeRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) attributeRule.get(CommonConstants.RULES_PROPERTY);
      attributeRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      attributeRule.remove(CommonConstants.RULES_PROPERTY);
      attributeRule.remove(ConfigTag.entityType.toString());
      attributeRule.remove(ConfigTag.entityAttributeType.toString());
      Vertex attributeRuleVertex = UtilClass.createNode(attributeRule,
          attributeIntermediateVertexType);
      Vertex attribute = UtilClass.getVertexById(entityId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      attributeRuleVertex.addEdge(RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK, attribute);
      dataRule.addEdge(RelationshipLabelConstants.ATTRIBUTE_DATA_RULE, attributeRuleVertex);
      
      for (Map<String, Object> rule : rules) {
        String ruleListLinkId = (String) rule.get(CommonConstants.RULE_LIST_LINK_ID_PROPERTY);
        String attributeLinkId = (String) rule.get(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        List<String> klassLinkIds = (List<String>) rule.get(IDataRuleEntityRule.KLASS_LINK_IDS);
        rule.remove(CommonConstants.RULE_LIST_LINK_ID_PROPERTY);
        rule.remove(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        rule.remove(IDataRuleEntityRule.KLASS_LINK_IDS);
        rule.remove(ConfigTag.entityType.toString());
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType);
        attributeRuleVertex.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
        if (ruleListLinkId != null && !ruleListLinkId.isEmpty()) {
          Vertex ruleList = UtilClass.getVertexById(ruleListLinkId, VertexLabelConstants.RULE_LIST);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_RULE_LIST, ruleList);
        }
        if (attributeLinkId != null && !attributeLinkId.isEmpty()) {
          Vertex attributeToLink = UtilClass.getVertexById(attributeLinkId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK, attributeToLink);
        }
        if (klassLinkIds != null && klassLinkIds.size() > 0) {
          for (String klasslinkId : klassLinkIds) {
            Vertex klassToLink = UtilClass.getVertexById(klasslinkId,
                VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
            ruleNode.addEdge(RelationshipLabelConstants.HAS_KLASS_LINK, klassToLink);
          }
        }
      }
    }
  }
  
  public static void addRuleViolations(Vertex dataRule, List<Map<String, Object>> ruleViolations)
      throws Exception
  {
    OrientVertexType ruleViolationVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.RULE_VIOLATION, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> ruleViolation : ruleViolations) {
      String entityId = (String) ruleViolation.get(CommonConstants.ENTITY_ID_PROPERTY);
      String type = (String) ruleViolation.get(CommonConstants.TYPE_PROPERTY);
      String entityConstant = getEntityConstants(type);
      Vertex ruleViolationVertex = UtilClass.createNode(ruleViolation, ruleViolationVertexType);
      Vertex entity = UtilClass.getVertexById(entityId, entityConstant);
      ruleViolationVertex.addEdge(RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK, entity);
      dataRule.addEdge(RelationshipLabelConstants.RULE_VIOLATION_LINK, ruleViolationVertex);
    }
  }
  
  public static String getEntityConstants(String type)
  {
    String entityConstant = "";
    switch (type) {
      case "attribute":
        entityConstant = VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
        break;
      case "tag":
        entityConstant = VertexLabelConstants.ENTITY_TAG;
        break;
      case "role":
        entityConstant = VertexLabelConstants.ENTITY_TYPE_ROLE;
        break;
      case "relationship":
        entityConstant = VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP;
        break;
      case "type":
        entityConstant = VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
        break;
      case "taxonomy":
        entityConstant = VertexLabelConstants.ROOT_KLASS_TAXONOMY;
        break;
    }
    return entityConstant;
  }
  
  public static void addRoleRules(Vertex dataRule, List<Map<String, Object>> roleRules)
      throws Exception
  {
    OrientVertexType roleIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ROLE_RULE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> roleRule : roleRules) {
      String entityId = (String) roleRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) roleRule.get(CommonConstants.RULES_PROPERTY);
      roleRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      roleRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex roleRuleVertex = UtilClass.createNode(roleRule, roleIntermediateVertexType);
      Vertex role = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      roleRuleVertex.addEdge(RelationshipLabelConstants.ROLE_DATA_RULE_LINK, role);
      dataRule.addEdge(RelationshipLabelConstants.ROLE_DATA_RULE, roleRuleVertex);
      
      for (Map<String, Object> rule : rules) {
        List<String> values = (List<String>) rule.get(CommonConstants.VALUES_PROPERTY);
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType);
        roleRuleVertex.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          ruleNode.addEdge(RelationshipLabelConstants.RULE_USER_LINK, user);
        }
      }
    }
  }
  
  public static void addTypeRules(Vertex dataRule, List<Map<String, Object>> typeRules)
      throws Exception
  {
    OrientVertexType klassIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.KLASS_RULE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> typeRule : typeRules) {
      String entityId = (String) typeRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) typeRule.get(CommonConstants.RULES_PROPERTY);
      typeRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      typeRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex typeRuleVertex = UtilClass.createNode(typeRule, klassIntermediateVertexType);
      Vertex klass = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TYPE_KLASS);
      typeRuleVertex.addEdge(RelationshipLabelConstants.KLASS_DATA_RULE_LINK, klass);
      dataRule.addEdge(RelationshipLabelConstants.TYPE_DATA_RULE, typeRuleVertex);
      for (Map<String, Object> rule : rules) {
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType);
        typeRuleVertex.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
      }
    }
  }
  
  public static void addRelationshipRules(Vertex dataRule,
      List<Map<String, Object>> relationshipRules) throws Exception
  {
    OrientVertexType relationshipIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.RELATIONSHIP_RULE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> relationshipRule : relationshipRules) {
      String entityId = (String) relationshipRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) relationshipRule.get(CommonConstants.RULES_PROPERTY);
      relationshipRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      relationshipRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex relationshipRuleVertex = UtilClass.createNode(relationshipRule,
          relationshipIntermediateVertexType);
      Vertex relationship = UtilClass.getVertexById(entityId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      relationshipRuleVertex.addEdge(RelationshipLabelConstants.RELATIONSHIP_DATA_RULE_LINK,
          relationship);
      dataRule.addEdge(RelationshipLabelConstants.RELATIONSHIP_DATA_RULE, relationshipRuleVertex);
      for (Map<String, Object> rule : rules) {
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType);
        relationshipRuleVertex.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
      }
    }
  }
  
  public static void addTagRules(Vertex dataRule, List<Map<String, Object>> tagRules)
      throws Exception
  {
    OrientVertexType tagIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.TAG_RULE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> tagRule : tagRules) {
      String entityId = (String) tagRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) tagRule.get(CommonConstants.RULES_PROPERTY);
      tagRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      tagRule.remove(CommonConstants.RULES_PROPERTY);
      tagRule.remove(ConfigTag.entityType.toString());
      tagRule.remove(ConfigTag.entityAttributeType.toString());
      Vertex tagRuleVertex = UtilClass.createNode(tagRule, tagIntermediateVertexType);
      Vertex tag = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      tagRuleVertex.addEdge(RelationshipLabelConstants.TAG_DATA_RULE_LINK, tag);
      dataRule.addEdge(RelationshipLabelConstants.TAG_DATA_RULE, tagRuleVertex);
      for (Map<String, Object> rule : rules) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) rule
            .get(CommonConstants.TAG_VALUES);
        rule.remove(CommonConstants.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType);
        tagRuleVertex.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
          Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          ruleNode.addEdge(RelationshipLabelConstants.RULE_TAG_VALUE_LINK, tagValueNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(CommonConstants.TO_PROPERTY, to);
          propertyMap.put(CommonConstants.FROM_PROPERTY, from);
          idPropertyMap.put(id, propertyMap);
        }
        setTagValueEdgeProperty(idPropertyMap, ruleNode,
            RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
      }
    }
  }
  
  public static void setTagValueEdgeProperty(Map<String, Map<String, Object>> idPropertyMap,
      Vertex node, String edgeLabel)
  {
    Iterable<Edge> edges = node.getEdges(Direction.OUT, edgeLabel);
    for (Edge edge : edges) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      String tagId = tagValueNode.getProperty(CommonConstants.CODE_PROPERTY);
      Map<String, Object> propertyMap = new HashMap<>();
      propertyMap = (Map<String, Object>) idPropertyMap.get(tagId);
      int to = (int) propertyMap.get(CommonConstants.TO_PROPERTY);
      int from = (int) propertyMap.get(CommonConstants.FROM_PROPERTY);
      edge.setProperty(CommonConstants.TO_PROPERTY, to);
      edge.setProperty(CommonConstants.FROM_PROPERTY, from);
      edge.setProperty(CommonConstants.ENTITY_ID_PROPERTY, tagId);
    }
  }
  
  public static void setTagValueEdgePropertyForNormalization(
      Map<String, Map<String, Object>> idPropertyMap, Vertex node, String edgeLabel)
  {
    Iterable<Edge> edges = node.getEdges(Direction.OUT, edgeLabel);
    for (Edge edge : edges) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      String tagId = tagValueNode.getProperty(CommonConstants.CODE_PROPERTY);
      Map<String, Object> propertyMap = new HashMap<>();
      propertyMap = (Map<String, Object>) idPropertyMap.get(tagId);
      int to = (int) propertyMap.get(CommonConstants.TO_PROPERTY);
      int from = (int) propertyMap.get(CommonConstants.FROM_PROPERTY);
      String id = (String) propertyMap.get(CommonConstants.ID_PROPERTY);
      edge.setProperty(CommonConstants.TO_PROPERTY, to);
      edge.setProperty(CommonConstants.FROM_PROPERTY, from);
      edge.setProperty(IDataRuleTagValues.INNER_TAG_ID, tagId);
      edge.setProperty(CommonConstants.ENTITY_ID_PROPERTY, id);
    }
  }
  
  public static void addNormalizations(Vertex dataRule, List<Map<String, Object>> normalizations)
      throws Exception
  {
    OrientVertexType normalizationVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.NORMALIZATION, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> normalization : normalizations) {
      String entityId = (String) normalization.get(CommonConstants.ENTITY_ID_PROPERTY);
      String type = (String) normalization.get(CommonConstants.TYPE_PROPERTY);
      String entityConstant = getEntityConstants(type);
      Vertex normalizationVertex = UtilClass.createNode(normalization, normalizationVertexType,
          Arrays.asList(IAttributeValueNormalization.VALUE_ATTRIBUTE_ID,
              IConcatenatedNormalization.ATTRIBUTE_CONCATENATED_LIST));
      if (entityConstant.equals(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
          || entityConstant.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
        String relationShipLabel = getRelationshipLabel(entityConstant);
        List<String> entityIds = (List<String>) normalization.get(CommonConstants.VALUES_PROPERTY);
        if (!entityIds.isEmpty()) {
          dataRule.addEdge(relationShipLabel, normalizationVertex);
          for (String id : entityIds) {
            Vertex entity = UtilClass.getVertexById(id, entityConstant);
            normalizationVertex.addEdge(RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK,
                entity);
          }
        }
      }
      else {
        Vertex entity = UtilClass.getVertexById(entityId, entityConstant);
        normalizationVertex.addEdge(RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK, entity);
        dataRule.addEdge(RelationshipLabelConstants.NORMALIZATION_LINK, normalizationVertex);
        if (type.equals("role")) {
          List<String> values = (List<String>) normalization.get(CommonConstants.VALUES_PROPERTY);
          normalizationVertex.removeProperty(CommonConstants.VALUES_PROPERTY);
          for (String userId : values) {
            Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
            normalizationVertex.addEdge(RelationshipLabelConstants.NORMALIZATION_USER_LINK, user);
          }
        }
        else if (type.equals("tag")) {
          List<Map<String, Object>> tagValues = (List<Map<String, Object>>) normalization
              .get(CommonConstants.TAG_VALUES);
          normalizationVertex.removeProperty(CommonConstants.TAG_VALUES);
          Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
          for (Map<String, Object> tagValue : tagValues) {
            int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
            int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
            String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
            String innerTagId = (String) tagValue.get(IDataRuleTagValues.INNER_TAG_ID);
            innerTagId = innerTagId == null ? id : innerTagId;
            Vertex tagValueNode = UtilClass.getVertexById(innerTagId,
                VertexLabelConstants.ENTITY_TAG);
            normalizationVertex.addEdge(RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK,
                tagValueNode);
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put(CommonConstants.TO_PROPERTY, to);
            propertyMap.put(CommonConstants.FROM_PROPERTY, from);
            propertyMap.put(CommonConstants.ID_PROPERTY, id);
            idPropertyMap.put(innerTagId, propertyMap);
          }
          
          setTagValueEdgePropertyForNormalization(idPropertyMap, normalizationVertex,
              RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
        }
        else if (type.equals("attribute")) {
          String transformationType = (String) normalization
              .get(INormalization.TRANSFORMATION_TYPE);
          if (transformationType.equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
            addAttributeToNormalization(normalizationVertex, normalization);
          }
          else if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
            addConcatenationToNormalization(normalizationVertex, normalization);
          }
          List<String> values = (List<String>) normalization.get(INormalization.VALUES);
          if (values != null) {
            normalizationVertex.setProperty(CommonConstants.VALUES_PROPERTY, values);
          }
          String valueAsHtml = (String) normalization.get(INormalization.VALUE_AS_HTML);
          if (valueAsHtml != null) {
            normalizationVertex.setProperty(CommonConstants.VALUES_PROPERTY, values);
          }
        }
      }
    }
  }
  
  public static void addAttributeToNormalization(Vertex normalizationVertex,
      Map<String, Object> normalization) throws Exception
  {
    String attributeValueID = (String) normalization
        .get(IAttributeValueNormalization.VALUE_ATTRIBUTE_ID);
    if (attributeValueID != null && !attributeValueID.isEmpty()) {
      try {
        Vertex attributeNode = UtilClass.getVertexByIndexedId(attributeValueID,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        normalizationVertex.addEdge(RelationshipLabelConstants.NORMALIZATION_ATTRIBUTE_VALUE_LINK,
            attributeNode);
      }
      catch (NotFoundException e) {
        throw new AttributeNotFoundException();
      }
    }
  }
  
  public static void addConcatenationToNormalization(Vertex normalizationVertex,
      Map<String, Object> normalization) throws Exception
  {
    List<Map<String, Object>> attributeConcatenatedList = (List<Map<String, Object>>) normalization
        .get(IConcatenatedNormalization.ATTRIBUTE_CONCATENATED_LIST);
    if (attributeConcatenatedList != null && attributeConcatenatedList.size() > 0) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.CONCATENATED_NODE, CommonConstants.CODE_PROPERTY);
      for (Map<String, Object> operator : attributeConcatenatedList) {
        String concatenatedOperatorType = (String) operator.get(IConcatenatedOperator.TYPE);
        Vertex concatenatedVertex = UtilClass.createNode(operator, vertexType,
            new ArrayList<>(Arrays.asList(IConcatenatedAttributeOperator.ATTRIBUTE_ID,
                IConcatenatedTagOperator.TAG_ID)));
        if (concatenatedOperatorType.equals(CommonConstants.ATTRIBUTE)) {
          String attributeId = (String) operator.get(IConcatenatedAttributeOperator.ATTRIBUTE_ID);
          if (attributeId != null) {
            try {
              normalizationVertex.addEdge(
                  RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK, concatenatedVertex);
              Vertex attributeVertex = UtilClass.getVertexByIndexedId(attributeId,
                  VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
              concatenatedVertex.addEdge(
                  RelationshipLabelConstants.NORMALIZATION_CONCATENATED_NODE_ATTRIBUTE_LINK,
                  attributeVertex);
            }
            catch (NotFoundException e) {
              throw new AttributeNotFoundException();
            }
          }
        }
        else if (concatenatedOperatorType.equals(CommonConstants.TAG)) {
          String tagId = (String) operator.get(IConcatenatedTagOperator.TAG_ID);
          if (tagId != null) {
            try {
              normalizationVertex.addEdge(RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK,
                  concatenatedVertex);
              Vertex tagVertex = UtilClass.getVertexByIndexedId(tagId,
                  VertexLabelConstants.ENTITY_TAG);
              concatenatedVertex.addEdge(
                  RelationshipLabelConstants.NORMALIZATION_CONCATENATED_NODE_TAG_LINK, tagVertex);
            }
            catch (NotFoundException e) {
              throw new TagNotFoundException();
            }
          }
        }
        else {
          normalizationVertex.addEdge(RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK,
              concatenatedVertex);
        }
      }
    }
  }
  
  public static void createRuleTagValueEdges(Vertex ruleNode, List<Map<String, Object>> tagValues)
      throws Exception
  {
    
    Iterable<Edge> edges = ruleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
    if (!(edges.iterator()
        .hasNext())) {
      for (Map<String, Object> tagValue : tagValues) {
        String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
        Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        ruleNode.addEdge(RelationshipLabelConstants.RULE_TAG_VALUE_LINK, tagValueNode);
      }
    }
  }
  
  public static void addKlassRules(Vertex dataRuleNode, List<String> klassIds) throws Exception
  {
    // OrientVertexType klassIntermediateVertexType =
    // UtilClass.getOrCreateVertexType(
    // VertexLabelConstants.KLASS_RULE_INTERMEDIATE,
    // CommonConstants.CODE_PROPERTY);
    // if (!klassIds.isEmpty()) {
    // Vertex klassIntermediateVertex = UtilClass.createNode(new HashMap<>(),
    // klassIntermediateVertexType, new ArrayList<>());
    // dataRuleNode.addEdge(RelationshipLabelConstants.TYPE_DATA_RULE,
    // klassIntermediateVertex);
    for (String klassId : klassIds) {
      Vertex klassVertex = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      klassVertex.addEdge(RelationshipLabelConstants.HAS_KLASS_RULE_LINK, dataRuleNode);
    }
    // }
  }
  
  public static void addTaxonomyRules(Vertex dataRuleNode, List<String> taxonomyIds)
      throws Exception
  {
    // OrientVertexType klassIntermediateVertexType =
    // UtilClass.getOrCreateVertexType(
    // VertexLabelConstants.TAXONOMY_RULE_INTERMEDIATE,
    // CommonConstants.CODE_PROPERTY);
    // if (!taxonomyIds.isEmpty()) {
    // Vertex klassIntermediateVertex = UtilClass.createNode(new HashMap<>(),
    // klassIntermediateVertexType, new ArrayList<>());
    // dataRuleNode.addEdge(RelationshipLabelConstants.TAXONOMY_DATA_RULE,
    // klassIntermediateVertex);
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      taxonomyVertex.addEdge(RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK, dataRuleNode);
    }
    // }
  }
  
  public static void addLanguages(Vertex dataRuleNode, List<String> languages) throws Exception
  {
    
    if (languages == null || languages.isEmpty()) {
      return;
    }
    
    for (String languageId : languages) {
      Vertex languageVertex = UtilClass.getVertexByCode(languageId, VertexLabelConstants.LANGUAGE);
      dataRuleNode.addEdge(RelationshipLabelConstants.RULE_LANGUAGE_LINK, languageVertex);
    }
  }
  
  public static void manageDeletedLanguages(Vertex dataRule, List<String> deletedLanguages)
  {
    List<Edge> edgesToRemove = new ArrayList<>();
    List<String> deletedLanguagesClone = new ArrayList<>(deletedLanguages);
    Iterable<Edge> availableLanguageEdges = dataRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.RULE_LANGUAGE_LINK);
    for (Edge availableLanguageEdge : availableLanguageEdges) {
      Vertex LanguageVertex = availableLanguageEdge.getVertex(Direction.IN);
      String languageCode = UtilClass.getCode(LanguageVertex);
      if (deletedLanguagesClone.contains(languageCode)) {
        edgesToRemove.add(availableLanguageEdge);
        deletedLanguagesClone.remove(languageCode);
      }
      if (deletedLanguagesClone.isEmpty()) {
        break;
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }
  
  public static String getRelationshipLabel(String entityLabel)
  {
    switch (entityLabel) {
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
        return RelationshipLabelConstants.TAXONOMY_NORMALIZATION_LINK;
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
        return RelationshipLabelConstants.TYPE_NORMALIZATION_LINK;
    }
    return null;
  }
  
  public static Vertex getAttributeNodeFromNormalization(Vertex normalization) throws Exception
  {
    Iterator<Vertex> valueAttributeIterator = normalization
        .getVertices(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_ATTRIBUTE_VALUE_LINK)
        .iterator();
    return UtilClass.getVertexFromIterator(valueAttributeIterator, true);
  }

  public static Map<String, Vertex> getLanguagesForDataRules(Vertex dataRuleNode)
  {
    Map<String, Vertex> languages = new HashMap<>();
    Iterable<Vertex> vertices = dataRuleNode.getVertices(Direction.OUT, RelationshipLabelConstants.RULE_LANGUAGE_LINK);
    for(Vertex vertex : vertices){
      String code = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      languages.put(code, vertex);
    }
    return languages;
  }

  public static Map<String, Vertex> getOrganizationsForRule(Vertex dataRule)
  {
    Map<String, Vertex> organizations = new HashMap<>();
    Iterable<Vertex> vertices = dataRule.getVertices(Direction.OUT, RelationshipLabelConstants.ORGANISATION_RULE_LINK);
    for (Vertex vertex : vertices) {
      String code = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      organizations.put(code, vertex);
    }
    return organizations;
  }
}
