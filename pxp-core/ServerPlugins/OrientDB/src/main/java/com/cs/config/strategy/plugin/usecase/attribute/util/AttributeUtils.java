package com.cs.config.strategy.plugin.usecase.attribute.util;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.*;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.attribute.IUpdatedAttributeListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import java.util.*;

@SuppressWarnings("unchecked")
public class AttributeUtils {
  
  public static final List<String> fieldsToExclude                   = Arrays.asList(ISaveAttributeModel.ATTRIBUTE_TAGS);
  
  public static final List<String> LANGUAGE_DEPENDENT_ATTRIBUTE_TYPE = Arrays.asList(CommonConstants.TEXT_ATTRIBUTE_TYPE,
      CommonConstants.HTML_TYPE_ATTRIBUTE, CommonConstants.CONCATENATED_ATTRIBUTE_TYPE);
  
  public static Vertex createAttribute(Map<String, Object> attributeMap, OrientGraph graph)
      throws Exception
  {
    
    String type = (String) attributeMap.get(IAttribute.TYPE);
    Vertex vertex = null;
    
    // Allow only language dependent attribute type to toggle
    if (!LANGUAGE_DEPENDENT_ATTRIBUTE_TYPE.contains(type)) {
      attributeMap.put(IConcatenatedAttribute.IS_TRANSLATABLE, false);
    }
    
    if (type.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
      vertex = AttributeUtils.createCalculatedAttribute(attributeMap, graph);
    }
    else if (type.equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
      vertex = AttributeUtils.createConcatenatedAttribute(attributeMap, graph);
    }
    else {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, CommonConstants.CODE_PROPERTY);
      vertex = UtilClass.createNode(attributeMap, vertexType, fieldsToExclude);
    }
    return vertex;
  }
  
  public static Map<String, Object> getAttributeMap(Vertex attributeNode)
  {
    Map<String, Object> attributeMap = new HashMap<>();
    
    String type = attributeNode.getProperty(IAttribute.TYPE);
    if (type.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
      attributeMap = AttributeUtils.getCalculatedAttribute(attributeNode);
    }
    else if (type.equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
      attributeMap = AttributeUtils.getConcatenatedAttribute(attributeNode);
    }
    else {
      attributeMap.putAll(UtilClass.getMapFromNode(attributeNode));
    }
    
    Iterator<Edge> iterator = attributeNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
        .iterator();
    Edge validatorOfRelationship = null;
    while (iterator.hasNext()) {
      validatorOfRelationship = iterator.next();
      Map<String, Object> validatorMap = new HashMap<>();
      Vertex validatorNode = validatorOfRelationship
          .getVertex(com.tinkerpop.blueprints.Direction.OUT);
      validatorMap.putAll(UtilClass.getMapFromNode(validatorNode));
      attributeMap.put(CommonConstants.VALIDATOR_PROPERTY, validatorMap);
    }
    return attributeMap;
  }
  
  public static Map<String, Object> createAttributeData(Map<String, Object> attribute, List<Map<String, Object>> auditInfoList)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    if (ValidationUtils.validateAttributeInfo(attribute)) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, CommonConstants.CODE_PROPERTY);
      
      String type = (String) attribute.get(IAttribute.TYPE);
      Vertex vertex = null;
      
      if (type.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
        vertex = AttributeUtils.createCalculatedAttribute(attribute, graph);
        returnMap = AttributeUtils.getCalculatedAttribute(vertex);
      }
      else {
        vertex = UtilClass.createNode(attribute, vertexType);
        returnMap.putAll(UtilClass.getMapFromNode(vertex));
      }
      AuditLogUtils.fillAuditLoginfo(auditInfoList, vertex, Entities.PROPERTIES, Elements.ATTRIBUTE);
      graph.commit();
    }
    
    return returnMap;
  }
  
  public static Vertex createCalculatedAttribute(Map<String, Object> attributeMap,
      OrientGraph graph) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, CommonConstants.CODE_PROPERTY);
    List<Map<String, Object>> attributeOperatorList = new ArrayList<>();
    attributeOperatorList = (List<Map<String, Object>>) attributeMap
        .get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
    attributeMap.remove(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
    //Vertex vertex = UtilClass.createNode(attributeMap, vertexType);
    Vertex vertex = UtilClass.createNode(attributeMap, vertexType, new ArrayList<>());
    createOperatorNodes(vertex, attributeOperatorList);
    return vertex;
  }
  
  public static void createOperatorNodes(Vertex vertex,
      List<Map<String, Object>> attributeOperatorList) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.CALCULATED_ATTRIBUTE_OPERATOR, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> attributeOperator : attributeOperatorList) {
      // attributeId is the id of the operator attribute
      String attributeId = (String) attributeOperator.get(IAttributeOperator.ATTRIBUTE_ID);
      attributeOperator.remove(IAttributeOperator.ATTRIBUTE_ID);
      Vertex operatorVertex = UtilClass.createNode(attributeOperator, vertexType);
      vertex.addEdge(RelationshipLabelConstants.ATTRIBUTE_OPERATOR_LINK, operatorVertex);
      if (attributeId != null) {
        Vertex attributeVertex = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        operatorVertex.addEdge(RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK, attributeVertex);
      }
    }
  }
  
  public static HashMap<String, Object> getCalculatedAttribute(Vertex vertex)
  {
    HashMap<String, Object> returnMap = UtilClass.getMapFromNode(vertex);
    Iterable<Vertex> operators = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ATTRIBUTE_OPERATOR_LINK);
    List<Map<String, Object>> attributeOperatorList = new ArrayList<>();
    for (Vertex operator : operators) {
      String attributeId = null;
      HashMap<String, Object> operatorMap = UtilClass.getMapFromNode(operator);
      Iterable<Vertex> attributeVertices = operator.getVertices(Direction.OUT,
          RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK);
      for (Vertex attributeVertex : attributeVertices) {
        attributeId = attributeVertex.getProperty(CommonConstants.CODE_PROPERTY);
      }
      operatorMap.put(IAttributeOperator.ATTRIBUTE_ID, attributeId);
      attributeOperatorList.add(operatorMap);
    }
    List<Map<String, Object>> sortedAttributeOperatorList = getSortedAttributeOperatorList(
        attributeOperatorList);
    returnMap.put(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST, sortedAttributeOperatorList);
    return returnMap;
  }
  
  public static List<Map<String, Object>> getSortedAttributeOperatorList(
      List<Map<String, Object>> attributeOperatorList)
  {
    List<Map<String, Object>> sortedAttributeOperatorList = new ArrayList<>();
    Map<Integer, Object> orderMap = new HashMap<>();
    int listSize = attributeOperatorList.size();
    for (Map<String, Object> map : attributeOperatorList) {
      Integer order = (Integer) map.get(IAttributeOperator.ORDER);
      orderMap.put(order, map);
    }
    for (int index = 0; index < listSize; index++) {
      Map<String, Object> map = (Map<String, Object>) orderMap.get(index);
      if (map != null) {
        sortedAttributeOperatorList.add(map);
      }
    }
    return sortedAttributeOperatorList;
  }
  
  public static Map<String, Object> saveCalculatedAttribute(Vertex vertex,
      Map<String, Object> attributeMap, List<Map<String, Object>> updatedCalculatedAttributeList) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> savedAttributeMap = new HashMap<>();
    List<Map<String, Object>> attributeOperatorList = new ArrayList<>();
    List<Map<String, Object>> savedAttributeOperatorList = new ArrayList<>();
    List<Map<String, Object>> addedAttributeOperatorList = new ArrayList<>();
    List<Map<String, Object>> modifiedAttributeOperatorList = new ArrayList<>();
    List<String> deletedAttributeOperatorList = new ArrayList<>();
    Map<String, Object> calculatedAttributeMap = new HashMap<>();
    
    attributeOperatorList = (List<Map<String, Object>>) attributeMap
        .get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
    
    savedAttributeMap = getCalculatedAttribute(vertex);
    savedAttributeOperatorList = (List<Map<String, Object>>) savedAttributeMap
        .get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
    
    fillAttributeOperatorADMLists(attributeOperatorList, savedAttributeOperatorList,
        addedAttributeOperatorList, modifiedAttributeOperatorList, deletedAttributeOperatorList);
    
    if (!addedAttributeOperatorList.isEmpty() || !modifiedAttributeOperatorList.isEmpty() || !deletedAttributeOperatorList.isEmpty()) {
      calculatedAttributeMap.put(IUpdatedAttributeListModel.PROPERTY_IID, savedAttributeMap.get(IAttribute.PROPERTY_IID));
      calculatedAttributeMap.put(IUpdatedAttributeListModel.ATTRIBUTE_ID, savedAttributeMap.get(IAttribute.CODE));
      updatedCalculatedAttributeList.add(calculatedAttributeMap);
    }
    
    List<String> fieldsToExclude = Arrays.asList(ISaveAttributeModel.ADDED_TAGS,
        ISaveAttributeModel.MODIFIED_TAGS, ISaveAttributeModel.DELETED_TAGS,
        ISaveAttributeModel.ATTRIBUTE_TAGS, ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST, ISaveAttributeModel.IS_TRANSLATABLE,
        ISaveAttributeModel.IS_STANDARD);
    
    vertex = UtilClass.saveNode(attributeMap, vertex, fieldsToExclude);
    
    handleAddedAttributeOperators(addedAttributeOperatorList, vertex);
    handleModifiedAttributeOperators(modifiedAttributeOperatorList, vertex);
    handleDeletedAttributeOperators(deletedAttributeOperatorList);
    
    returnMap = getCalculatedAttribute(vertex);
    
    return returnMap;
  }
  
  public static void fillAttributeOperatorADMLists(List<Map<String, Object>> attributeOperatorList,
      List<Map<String, Object>> savedAttributeOperatorList,
      List<Map<String, Object>> addedAttributeOperatorList,
      List<Map<String, Object>> modifiedAttributeOperatorList,
      List<String> deletedAttributeOperatorList)
  {
    Map<String, Object> attributeOperatorMap = getMapFromList(attributeOperatorList);
    Map<String, Object> savedAttributeOperatorMap = getMapFromList(savedAttributeOperatorList);
    
    // fill added operators list
    for (Map<String, Object> attributeOperator : attributeOperatorList) {
      String id = (String) attributeOperator.get(IAttributeOperator.ID);
      if (!savedAttributeOperatorMap.containsKey(id)) {
        addedAttributeOperatorList.add(attributeOperator);
      }
    }
    
    // fill deleted operators list
    for (Map<String, Object> savedAttributeOperator : savedAttributeOperatorList) {
      String id = (String) savedAttributeOperator.get(IAttributeOperator.ID);
      if (!attributeOperatorMap.containsKey(id)) {
        deletedAttributeOperatorList.add(id);
      }
    }
    
    // fill modified operators list
    for (Map<String, Object> savedOperator : savedAttributeOperatorList) {
      String id = (String) savedOperator.get(IAttributeOperator.ID);
      if (attributeOperatorMap.containsKey(id)) {
        Map<String, Object> attributeOperator = (Map<String, Object>) attributeOperatorMap.get(id);
        //Boolean isSame = savedOperator.equals(attributeOperator);
        Boolean isSame = isCalculationOperatorSame(savedOperator, attributeOperator);
        if (!isSame) {
          modifiedAttributeOperatorList.add(attributeOperator);
        }
      }
    }
  }
  
  public static Map<String, Object> getMapFromList(List<Map<String, Object>> list)
  {
    Map<String, Object> returnMap = new HashMap<>();
    for (Map<String, Object> map : list) {
      String id = (String) map.get(CommonConstants.ID_PROPERTY);
      returnMap.put(id, map);
    }
    return returnMap;
  }
  
  public static void handleAddedAttributeOperators(
      List<Map<String, Object>> addedAttributeOperatorList, Vertex vertex) throws Exception
  {
    createOperatorNodes(vertex, addedAttributeOperatorList);
  }
  
  public static void handleModifiedAttributeOperators(
      List<Map<String, Object>> modifiedAttributeOperatorList, Vertex vertex) throws Exception
  {
    for (Map<String, Object> attributeOperator : modifiedAttributeOperatorList) {
      String id = (String) attributeOperator.get(IAttributeOperator.ID);
      String attributeId = (String) attributeOperator.get(IAttributeOperator.ATTRIBUTE_ID);
      attributeOperator.remove(IAttributeOperator.ATTRIBUTE_ID);
      Vertex operator = UtilClass.getVertexById(id,
          VertexLabelConstants.CALCULATED_ATTRIBUTE_OPERATOR);
      UtilClass.saveNode(attributeOperator, operator);
      Iterable<Vertex> linkedAttributes = operator.getVertices(Direction.OUT,
          RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK);
      Vertex linkedAttributeNode = null;
      for (Vertex attribute : linkedAttributes) {
        linkedAttributeNode = attribute;
      }
      if (linkedAttributeNode != null) {
        String linkedAttributeId = linkedAttributeNode
            .getProperty(CommonConstants.CODE_PROPERTY);
        if (!linkedAttributeId.equals(attributeId)) {
          Iterable<Edge> operatorAttributeEdges = operator.getEdges(Direction.OUT,
              RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK);
          for (Edge operatorAttributeEdge : operatorAttributeEdges) {
            operatorAttributeEdge.remove();
          }
          if (attributeId != null) {
            Vertex newLinkedAttribute = UtilClass.getVertexById(attributeId,
                VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
            operator.addEdge(RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK,
                newLinkedAttribute);
          }
        }
      }
      else if (attributeId != null) {
        Vertex newLinkedAttribute = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        operator.addEdge(RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK, newLinkedAttribute);
      }
    }
  }
  
  public static void handleDeletedAttributeOperators(List<String> deletedAttributeOperatorList)
      throws Exception
  {
    for (String operatorId : deletedAttributeOperatorList) {
      Vertex operator = UtilClass.getVertexById(operatorId,
          VertexLabelConstants.CALCULATED_ATTRIBUTE_OPERATOR);
      operator.remove();
    }
  }
  
  public static void deleteOperatorNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> operators = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.ATTRIBUTE_OPERATOR_LINK);
    for (Vertex operator : operators) {
      operator.remove();
    }
  }
  
  public static void deleteConcatenatedNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> operators = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK);
    for (Vertex operator : operators) {
      operator.remove();
    }
    
    // delete tag operators
    operators = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK);
    for (Vertex operator : operators) {
      operator.remove();
    }
    
    // delete html operators
    operators = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK);
    for (Vertex operator : operators) {
      operator.remove();
    }
  }
  
  public static void createConcatenatedNodes(Vertex vertex,
      List<Map<String, Object>> concatenatedOperatorList) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.CONCATENATED_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> operator : concatenatedOperatorList) {
      String type = (String) operator.get(IConcatenatedOperator.TYPE);
      Vertex concatenatedVertex = UtilClass.createNode(operator, vertexType, new ArrayList<>(Arrays
          .asList(IConcatenatedAttributeOperator.ATTRIBUTE_ID, IConcatenatedTagOperator.TAG_ID)));
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        // attributeId is the id of the operator attribute
        vertex.addEdge(RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK,
            concatenatedVertex);
        String attributeId = (String) operator.get(IConcatenatedAttributeOperator.ATTRIBUTE_ID);
        if (attributeId != null) {
          Vertex attributeVertex = UtilClass.getVertexById(attributeId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          concatenatedVertex.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK,
              attributeVertex);
        }
      }
      else if (type.equals(CommonConstants.TAG)) {
        vertex.addEdge(RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK, concatenatedVertex);
        String tagId = (String) operator.get(IConcatenatedTagOperator.TAG_ID);
        if (tagId != null) {
          Vertex tagVertex = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
          concatenatedVertex.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK,
              tagVertex);
        }
      }
      else {
        vertex.addEdge(RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK, concatenatedVertex);
      }
    }
  }
  
  public static List<Map<String, Object>> getSortedAttributeConcatenatedList(
      List<Map<String, Object>> attributeConcatenatedList)
  {
    List<Map<String, Object>> sortedAttributeConcatenatedList = new ArrayList<>();
    Map<Integer, Object> orderMap = new HashMap<>();
    int listSize = attributeConcatenatedList.size();
    for (Map<String, Object> map : attributeConcatenatedList) {
      Integer order = (Integer) map.get(IAttributeOperator.ORDER);
      orderMap.put(order, map);
    }
    for (int index = 0; index < listSize; index++) {
      Map<String, Object> map = (Map<String, Object>) orderMap.get(index);
      if (map != null) {
        sortedAttributeConcatenatedList.add(map);
      }
    }
    return sortedAttributeConcatenatedList;
  }
  
  public static HashMap<String, Object> getConcatenatedAttribute(Vertex vertex)
  {
    HashMap<String, Object> returnMap = UtilClass.getMapFromNode(vertex);
    List<Map<String, Object>> entityConcatenatedList = new ArrayList<>();
    
    fillConcatenatedAttribute(vertex, entityConcatenatedList);
    
    fillConcatenatedTag(vertex, entityConcatenatedList);
    
    fillConcatenatedHtml(vertex, entityConcatenatedList);
    
    List<Map<String, Object>> sortedAttributeConcatenatedList = getSortedAttributeConcatenatedList(
        entityConcatenatedList);
    returnMap.put(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST,
        sortedAttributeConcatenatedList);
    return returnMap;
  }
  
  private static void fillConcatenatedHtml(Vertex vertex,
      List<Map<String, Object>> entityConcatenatedList)
  {
    Iterable<Vertex> concatenatedHtmlNodes = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK);
    for (Vertex node : concatenatedHtmlNodes) {
      entityConcatenatedList.add(UtilClass.getMapFromNode(node));
    }
  }
  
  private static void fillConcatenatedTag(Vertex vertex,
      List<Map<String, Object>> entityConcatenatedList)
  {
    Iterable<Vertex> concatenatedTagNodes = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK);
    for (Vertex node : concatenatedTagNodes) {
      String tagId = null;
      HashMap<String, Object> concatenatedNodeMap = UtilClass.getMapFromNode(node);
      Iterable<Vertex> tagVertices = node.getVertices(Direction.OUT,
          RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK);
      for (Vertex tagVertex : tagVertices) {
        tagId = tagVertex.getProperty(CommonConstants.CODE_PROPERTY);
      }
      concatenatedNodeMap.put(IConcatenatedTagOperator.TAG_ID, tagId);
      entityConcatenatedList.add(concatenatedNodeMap);
    }
  }
  
  private static void fillConcatenatedAttribute(Vertex vertex,
      List<Map<String, Object>> entityConcatenatedList)
  {
    Iterable<Vertex> concatenatedNodes = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK);
    for (Vertex node : concatenatedNodes) {
      String attributeId = null;
      HashMap<String, Object> concatenatedNodeMap = UtilClass.getMapFromNode(node);
      Iterable<Vertex> attributeVertices = node.getVertices(Direction.OUT,
          RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK);
      for (Vertex attributeVertex : attributeVertices) {
        attributeId = attributeVertex.getProperty(CommonConstants.CODE_PROPERTY);
      }
      concatenatedNodeMap.put(IConcatenatedAttributeOperator.ATTRIBUTE_ID, attributeId);
      entityConcatenatedList.add(concatenatedNodeMap);
    }
  }
  
  public static void fillAttributeConcatenatedADMLists(
      List<Map<String, Object>> attributeConcatenatedList,
      List<Map<String, Object>> savedAttributeConcatenatedList,
      List<Map<String, Object>> addedAttributeConcatenatedList,
      List<Map<String, Object>> modifiedAttributeConcatenatedList,
      List<String> deletedAttributeConcatenatedList)
  {
    Map<String, Object> attributeConcatenatedNodeMap = getMapFromList(attributeConcatenatedList);
    Map<String, Object> savedAttributeConcatenatedNodeMap = getMapFromList(
        savedAttributeConcatenatedList);
    
    // fill added operators list
    for (Map<String, Object> attributeOperator : attributeConcatenatedList) {
      String id = (String) attributeOperator.get(IConcatenatedOperator.ID);
      if (!savedAttributeConcatenatedNodeMap.containsKey(id)) {
        addedAttributeConcatenatedList.add(attributeOperator);
      }
    }
    
    // fill deleted operators list
    for (Map<String, Object> savedAttributeOperator : savedAttributeConcatenatedList) {
      String id = (String) savedAttributeOperator.get(IConcatenatedOperator.ID);
      if (!attributeConcatenatedNodeMap.containsKey(id)) {
        deletedAttributeConcatenatedList.add(id);
      }
    }
    
    // fill modified operators list
    for (Map<String, Object> savedOperator : savedAttributeConcatenatedList) {
      String id = (String) savedOperator.get(IConcatenatedOperator.ID);
      if (attributeConcatenatedNodeMap.containsKey(id)) {
        Map<String, Object> attributeOperator = (Map<String, Object>) attributeConcatenatedNodeMap
            .get(id);
        //Boolean isSame = savedOperator.equals(attributeOperator);
        Boolean isSame = checkIfOperatorsSame(savedOperator, attributeOperator);
        if (!isSame) {
          modifiedAttributeConcatenatedList.add(attributeOperator);
        }
      }
    }
  }
  
  public static Vertex createConcatenatedAttribute(Map<String, Object> attributeMap,
      OrientGraph graph) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, CommonConstants.CODE_PROPERTY);
    List<Map<String, Object>> concatenatedOperatorList = new ArrayList<>();
    concatenatedOperatorList = (List<Map<String, Object>>) attributeMap
        .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
    attributeMap.remove(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
    attributeMap.put(IConcatenatedAttribute.IS_TRANSLATABLE, true);
    //Vertex vertex = UtilClass.createNode(attributeMap, vertexType);
    Vertex vertex = UtilClass.createNode(attributeMap, vertexType, new ArrayList<>());
    createConcatenatedNodes(vertex, concatenatedOperatorList);
    return vertex;
  }
  
  public static void handleAddedAttributeConcatenatedList(
      List<Map<String, Object>> addedAttributeConcatenatedList, Vertex vertex) throws Exception
  {
    createConcatenatedNodes(vertex, addedAttributeConcatenatedList);
  }
  
  public static void handleModifiedAttributeConcatenatedList(
      List<Map<String, Object>> modifiedAttributeConcatenatedList, Vertex vertex) throws Exception
  {
    for (Map<String, Object> concatenatedElement : modifiedAttributeConcatenatedList) {
      String type = (String) concatenatedElement.get(IConcatenatedOperator.TYPE);
      String id = (String) concatenatedElement.get(IConcatenatedOperator.ID);
      
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        handleModifiedAttribute(concatenatedElement, id);
      }
      else if (type.equals(CommonConstants.TAG)) {
        handleModifiedTag(concatenatedElement, id);
      }
      else {
        Vertex concatenatedNode = UtilClass.getVertexById(id,
            VertexLabelConstants.CONCATENATED_NODE);
        UtilClass.saveNode(concatenatedElement, concatenatedNode);
      }
    }
  }
  
  private static void handleModifiedTag(Map<String, Object> concatenatedElement, String id)
      throws Exception
  {
    String tagId = (String) concatenatedElement.remove(IConcatenatedTagOperator.TAG_ID);
    Vertex concatenatedNode = UtilClass.getVertexById(id, VertexLabelConstants.CONCATENATED_NODE);
    UtilClass.saveNode(concatenatedElement, concatenatedNode);
    Iterable<Vertex> linkedTags = concatenatedNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK);
    Vertex linkedTagNode = null;
    for (Vertex tag : linkedTags) {
      linkedTagNode = tag;
    }
    if (linkedTagNode != null) {
      String linkedTagId = linkedTagNode.getProperty(CommonConstants.CODE_PROPERTY);
      if (!linkedTagId.equals(tagId)) {
        Iterable<Edge> concatenatedAttributeEdges = concatenatedNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK);
        for (Edge concatenateAttributeEdge : concatenatedAttributeEdges) {
          concatenateAttributeEdge.remove();
        }
        if (tagId != null) {
          Vertex newLinkedAttribute = UtilClass.getVertexById(tagId,
              VertexLabelConstants.ENTITY_TAG);
          concatenatedNode.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK,
              newLinkedAttribute);
        }
      }
    }
    else if (tagId != null) {
      Vertex newLinkedAttribute = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      concatenatedNode.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK,
          newLinkedAttribute);
    }
  }
  
  private static void handleModifiedAttribute(Map<String, Object> concatenatedElement, String id)
      throws Exception
  {
    String attributeId = (String) concatenatedElement
        .remove(IConcatenatedAttributeOperator.ATTRIBUTE_ID);
    Vertex concatenatedNode = UtilClass.getVertexById(id, VertexLabelConstants.CONCATENATED_NODE);
    UtilClass.saveNode(concatenatedElement, concatenatedNode);
    Iterable<Vertex> linkedAttributes = concatenatedNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK);
    Vertex linkedAttributeNode = null;
    for (Vertex attribute : linkedAttributes) {
      linkedAttributeNode = attribute;
    }
    if (linkedAttributeNode != null) {
      String linkedAttributeId = linkedAttributeNode.getProperty(CommonConstants.CODE_PROPERTY);
      if (!linkedAttributeId.equals(attributeId)) {
        Iterable<Edge> concatenatedAttributeEdges = concatenatedNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK);
        for (Edge concatenateAttributeEdge : concatenatedAttributeEdges) {
          concatenateAttributeEdge.remove();
        }
        if (attributeId != null) {
          Vertex newLinkedAttribute = UtilClass.getVertexById(attributeId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          concatenatedNode.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK,
              newLinkedAttribute);
        }
      }
    }
    else if (attributeId != null) {
      Vertex newLinkedAttribute = UtilClass.getVertexById(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      concatenatedNode.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK,
          newLinkedAttribute);
    }
  }
  
  public static void handleDeletedAttributeConcatenatedList(
      List<String> deletedAttributeConcatenatedList) throws Exception
  {
    for (String concatenatedNodeId : deletedAttributeConcatenatedList) {
      Vertex concatenatedNode = UtilClass.getVertexById(concatenatedNodeId,
          VertexLabelConstants.CONCATENATED_NODE);
      concatenatedNode.remove();
    }
  }
  
  public static Map<String, Object> saveConcatenatedAttribute(Vertex vertex, Map<String, Object> attributeMap,
      List<Map<String, Object>> updatedConcatenatedAttributeList) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> savedAttributeMap = new HashMap<>();
    List<Map<String, Object>> attributeConcatenatedList = new ArrayList<>();
    List<Map<String, Object>> savedAttributeConcatenatedList = new ArrayList<>();
    List<Map<String, Object>> addedAttributeConcatenatedList = new ArrayList<>();
    List<Map<String, Object>> modifiedAttributeConcatenatedList = new ArrayList<>();
    List<String> deletedAttributeConcatenatedList = new ArrayList<>();
    Map<String, Object> updatedAttributeMap = new HashMap<>();
    
    attributeConcatenatedList = (List<Map<String, Object>>) attributeMap
        .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
    
    savedAttributeMap = getConcatenatedAttribute(vertex);
    savedAttributeConcatenatedList = (List<Map<String, Object>>) savedAttributeMap
        .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);

    attributeMap.put(IConcatenatedAttribute.IS_TRANSLATABLE, true);
    fillAttributeConcatenatedADMLists(attributeConcatenatedList, savedAttributeConcatenatedList,
        addedAttributeConcatenatedList, modifiedAttributeConcatenatedList,
        deletedAttributeConcatenatedList);
    
    if (!addedAttributeConcatenatedList.isEmpty() || !modifiedAttributeConcatenatedList.isEmpty()
        || !deletedAttributeConcatenatedList.isEmpty()) {
      updatedAttributeMap.put(IUpdatedAttributeListModel.ATTRIBUTE_CONCATENATED_LIST, savedAttributeConcatenatedList);
      updatedAttributeMap.put(IUpdatedAttributeListModel.PROPERTY_IID, attributeMap.get(IAttribute.PROPERTY_IID));
      updatedAttributeMap.put(IUpdatedAttributeListModel.ATTRIBUTE_ID, attributeMap.get(IAttribute.CODE));
      updatedConcatenatedAttributeList.add(updatedAttributeMap);
    }
    
    List<String> fieldsToExclude = Arrays.asList(ISaveAttributeModel.ADDED_TAGS,
        ISaveAttributeModel.MODIFIED_TAGS, ISaveAttributeModel.DELETED_TAGS,
        ISaveAttributeModel.ATTRIBUTE_TAGS, IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST, ISaveAttributeModel.IS_TRANSLATABLE,
        ISaveAttributeModel.IS_STANDARD);
    
    vertex = UtilClass.saveNode(attributeMap, vertex, fieldsToExclude);
    
    handleAddedAttributeConcatenatedList(addedAttributeConcatenatedList, vertex);
    handleModifiedAttributeConcatenatedList(modifiedAttributeConcatenatedList, vertex);
    handleDeletedAttributeConcatenatedList(deletedAttributeConcatenatedList);
    
    returnMap = getConcatenatedAttribute(vertex);
    
    return returnMap;
  }
  
  /**
   * Fills referenced attributes that are in the formula of calculated or
   * concatenated attribute
   *
   * @param referencedAttributes
   * @param entity
   * @throws Exception
   * @author Kshitij
   */
  public static void fillDependentAttributesForCalculatedOrConcatenatedAttributes(
      Map<String, Object> referencedAttributes, Map<String, Object> referencedTags,
      Map<String, Object> entity) throws Exception
  {
    if (entity.get(IAttribute.TYPE)
        .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)) {
      List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) entity
          .get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
      fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes,
          attributeOperatorList);
    }
    else if (entity.get(IAttribute.TYPE)
        .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
      List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) entity
          .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
      fillDependentEntitiesForConcatenatedAttributes(referencedAttributes, referencedTags,
          attributeOperatorList);
    }
  }
  
  /**
   * Iterates the attributeOperatorList and fills referenced attributes
   *
   * @param referencedAttributes
   * @param attributeOperatorList
   * @throws Exception
   * @author Kshitij
   */
  public static void fillDependentAttributesForCalculatedOrConcatenatedAttributes(
      Map<String, Object> referencedAttributes, List<Map<String, Object>> attributeOperatorList)
      throws Exception
  {
    for (Map<String, Object> attributeOperator : attributeOperatorList) {
      String operatorAttributeId = (String) attributeOperator.get(IAttributeOperator.ATTRIBUTE_ID);
      if (operatorAttributeId != null && !referencedAttributes.containsKey(operatorAttributeId)) {
        Vertex operatorAttributeNode;
        try {
          operatorAttributeNode = UtilClass.getVertexById(operatorAttributeId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
        referencedAttributes.put(operatorAttributeId, getAttributeMap(operatorAttributeNode));
      }
    }
  }
  
  private static void fillDependentEntitiesForConcatenatedAttributes(
      Map<String, Object> referencedAttributes, Map<String, Object> referencedTags,
      List<Map<String, Object>> operatorList) throws Exception
  {
    for (Map<String, Object> operator : operatorList) {
      String type = (String) operator.get(IConcatenatedOperator.TYPE);
      if (type.equals(Constants.ATTRIBUTE)) {
        String operatorAttributeId = (String) operator
            .get(IConcatenatedAttributeOperator.ATTRIBUTE_ID);
        if (operatorAttributeId != null && !referencedAttributes.containsKey(operatorAttributeId)) {
          Vertex operatorAttributeNode;
          try {
            operatorAttributeNode = UtilClass.getVertexById(operatorAttributeId,
                VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          }
          catch (NotFoundException e) {
            throw new AttributeNotFoundException();
          }
          referencedAttributes.put(operatorAttributeId, getAttributeMap(operatorAttributeNode));
        }
      }
      else if (type.equals(CommonConstants.TAG)) {
        String operatorTagId = (String) operator.get(IConcatenatedTagOperator.TAG_ID);
        if (operatorTagId != null && !referencedTags.containsKey(operatorTagId)) {
          Vertex operatorTagNode;
          try {
            operatorTagNode = UtilClass.getVertexById(operatorTagId,
                VertexLabelConstants.ENTITY_TAG);
            
          }
          catch (NotFoundException e) {
            throw new TagNotFoundException();
          }
          referencedTags.put(operatorTagId, TagUtils.getTagMap(operatorTagNode, true));
        }
      }
    }
  }
  
  private static Boolean checkIfOperatorsSame(Map<String, Object> savedOperator, Map<String, Object> attributeOperator)
  {
    Boolean isSame = false;
    String type = (String) savedOperator.get(IConcatenatedOperator.TYPE);
    Integer existingOrder = (Integer) savedOperator.get(IConcatenatedOperator.ORDER);
    Integer newOrder = (Integer) attributeOperator.get(IConcatenatedOperator.ORDER);
    
    switch (type) {
      case "attribute":
        if (existingOrder.equals(newOrder) && savedOperator.get(IConcatenatedAttributeOperator.ATTRIBUTE_ID)
            .equals(attributeOperator.get(IConcatenatedAttributeOperator.ATTRIBUTE_ID))) {
          isSame = true;
        }
        break;
      
      case "tag":
        if (existingOrder.equals(newOrder)
            && savedOperator.get(IConcatenatedTagOperator.TAG_ID).equals(attributeOperator.get(IConcatenatedTagOperator.TAG_ID))) {
          isSame = true;
        }
        break;
      
      case "html":
        // When user defined text is empty value as html is null.
        String savedValueAsHtml = savedOperator.get(IConcatenatedHtmlOperator.VALUE_AS_HTML) == null ? ""
            : (String) savedOperator.get(IConcatenatedHtmlOperator.VALUE_AS_HTML);
        String newValueAsHtml = attributeOperator.get(IConcatenatedHtmlOperator.VALUE_AS_HTML) == null ? ""
            : (String) attributeOperator.get(IConcatenatedHtmlOperator.VALUE_AS_HTML);
        if (existingOrder.equals(newOrder)
            && savedOperator.get(IConcatenatedHtmlOperator.VALUE).equals(attributeOperator.get(IConcatenatedHtmlOperator.VALUE))
            && savedValueAsHtml.equals(newValueAsHtml)) {
          isSame = true;
        }
        break;
    }
    
    return isSame;
  }
  
  private static Boolean isCalculationOperatorSame(Map<String, Object> savedOperator, Map<String, Object> attributeOperator)
  {
    Boolean isSame = false;
    String type = (String) savedOperator.get(IAttributeOperator.TYPE);
    Integer existingOrder = (Integer) savedOperator.get(IAttributeOperator.ORDER);
    Integer newOrder = (Integer) attributeOperator.get(IAttributeOperator.ORDER);
    
    switch (type) {
      case "ATTRIBUTE":
        if (existingOrder.equals(newOrder)
            && savedOperator.get(IAttributeOperator.ATTRIBUTE_ID).equals(attributeOperator.get(IAttributeOperator.ATTRIBUTE_ID))) {
          isSame = true;
        }
        break;
      
      case "VALUE":
        if (existingOrder.equals(newOrder)
            && savedOperator.get(IAttributeOperator.VALUE).equals(attributeOperator.get(IAttributeOperator.VALUE))) {
          isSame = true;
        }
        break;
      
      case "ADD":
      case "SUBTRACT":
      case "MULTIPLY":
      case "DIVIDE":
      case "OPENING_BRACKET":
      case "CLOSING_BRACKET":
        if (existingOrder.equals(newOrder)
            && savedOperator.get(IAttributeOperator.TYPE).equals(attributeOperator.get(IAttributeOperator.TYPE))
            && savedOperator.get("operator").equals(attributeOperator.get("operator"))) {
          isSame = true;
        }
        break;
    }
    
    return isSame;
  }
  
}
