package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.endpoint.IGetMappedEndpointRequestModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetMappedMappingResponseModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class GetAllMappedMappings extends AbstractOrientPlugin {
  
  public GetAllMappedMappings(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String currentUserId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> attributesFinalMappings = new HashMap<>();
    Map<String, Object> tagsFinalMappings = new HashMap<>();
    // Map<String, Object> attributesMappedMappings = new HashMap<>();
    Map<String, Object> tagsMappedMappings = new HashMap<>();
    Map<String, Object> classMappedMappings = new HashMap<>();
    Map<String, Object> taxonomyMappedMappings = new HashMap<>();
    List<String> ignoredKeys = new ArrayList<>();
    
    currentUserId = (String) map.get(IGetMappedEndpointRequestModel.CURRENT_USER_ID);
    List<String> fileHeaders = (List<String>) map.get(IGetMappedEndpointRequestModel.FILE_HEADERS);
    Map<String, List<String>> attributeMappings = new HashMap<>();
    Map<String, Map<String, Object>> tagMappings = new HashMap<>();
    Map<String, Map<String, Object>> classMappings = new HashMap<>();
    Map<String, Map<String, Object>> taxonomyMappings = new HashMap<>();
    Map<String, Object> attributeMapForIdAndLabel = new HashMap<>();
    Map<String, Object> tagMapForIdAndLabel = new HashMap<>();
    Map<String, Object> taxonomyMapForIdAndLabel = new HashMap<>();
    Map<String, Object> klassMapForIdAndLabel = new HashMap<>();
    
    String endpointId = fillAttributeAndTagMappings(currentUserId, attributeMappings, tagMappings,
        classMappings, taxonomyMappings, ignoredKeys, attributeMapForIdAndLabel,
        tagMapForIdAndLabel, taxonomyMapForIdAndLabel, klassMapForIdAndLabel);
    
    // fillEntityMappingsAndIgnoredKeys(attributeMappings, ignoredKeys,
    // attributesMappedMappings);
    fillEntityMappingsAndIgnoredKeys(tagMappings, ignoredKeys, tagsMappedMappings);
    fillEntityMappingsAndIgnoredKeys(classMappings, ignoredKeys, classMappedMappings);
    fillEntityMappingsAndIgnoredKeys(taxonomyMappings, ignoredKeys, taxonomyMappedMappings);
    
    List<String> entitiesToSearch = new ArrayList<>();
    for (String header : fileHeaders) {
      if (!ignoredKeys.contains(header)) {
        if (attributeMappings.containsKey(header)) {
          attributesFinalMappings.put(header, attributeMappings.get(header));
        }
        else if (tagsMappedMappings.containsKey(header)) {
          tagsFinalMappings.put(header, tagsMappedMappings.get(header));
        }
        else {
          entitiesToSearch.add("'" + header + "'");
        }
      }
    }
    
    Iterable<Vertex> resultIterableForAttributes = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
            + " where " + CommonConstants.CODE_PROPERTY + " in " + entitiesToSearch.toString()))
        .execute();
    
    Iterable<Vertex> resultIterableForTags = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG + " where "
            + CommonConstants.CODE_PROPERTY + " in " + entitiesToSearch))
        .execute();
    
    for (Vertex vertex : resultIterableForAttributes) {
      String key = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      attributesFinalMappings.put(key, Arrays.asList(key));
    }
    
    for (Vertex vertex : resultIterableForTags) {
      String key = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      tagsFinalMappings.put(key, key);
    }
    
    List<String> validHeadersList = new ArrayList<>();
    validHeadersList.addAll(attributesFinalMappings.keySet());
    validHeadersList.addAll(tagsFinalMappings.keySet());
    
    returnMap.put(IGetMappedMappingResponseModel.ATTRIBUTES, attributesFinalMappings);
    returnMap.put(IGetMappedMappingResponseModel.TAGS, tagsFinalMappings);
    returnMap.put(IGetMappedMappingResponseModel.CLASSES, classMappedMappings);
    returnMap.put(IGetMappedMappingResponseModel.TAXONOMIES, taxonomyMappedMappings);
    returnMap.put(IGetMappedMappingResponseModel.HEADERS_TO_READ, validHeadersList);
    returnMap.put(IGetMappedMappingResponseModel.ENDPOINT_ID, endpointId);
    
    return returnMap;
  }
  
  private String fillAttributeAndTagMappings(String currentUserId,
      Map<String, List<String>> attributeMap, Map<String, Map<String, Object>> tagMap,
      Map<String, Map<String, Object>> classMappings,
      Map<String, Map<String, Object>> taxonomyMappings, List<String> ignoredKeys,
      Map<String, Object> attributeMapForIdAndLabel, Map<String, Object> tagMapForIdAndLabel,
      Map<String, Object> taxonomyMapForIdAndLabel, Map<String, Object> klassMapForIdAndLabel)
      throws Exception
  {
    String endpointId = null;
    HashMap<String, Object> masterMap = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex userNode = UtilClass.getVertexByIndexedId(currentUserId,
          VertexLabelConstants.ONBOARDING_USER);
      Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
      for (Vertex roleNode : roleNodes) {
        Iterable<Vertex> iteratorNode = roleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_ENDPOINT);
        
        for (Vertex endpoint : iteratorNode) {
          HashMap<String, Object> returnMap = new HashMap<>();
          attributeMap.putAll(getAttributeMapping(endpoint,
              RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, ignoredKeys));
          
          Iterable<Vertex> propetiesConfigRuleNodes = endpoint.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
          Iterable<Vertex> contextTagConfigRuleNodes = endpoint.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
          
          tagMap.putAll(MappingUtils.getTagMapping(propetiesConfigRuleNodes, new ArrayList<>(), tagMapForIdAndLabel));
          tagMap.putAll(MappingUtils.getTagMapping(contextTagConfigRuleNodes, new ArrayList<>(), tagMapForIdAndLabel));
          
          classMappings.putAll(MappingUtils.getEntityMapping(endpoint,
              RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE, new ArrayList<>(),
              klassMapForIdAndLabel));
          taxonomyMappings.putAll(MappingUtils.getEntityMapping(endpoint,
              RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE, new ArrayList<>(),
              taxonomyMapForIdAndLabel));
          endpointId = UtilClass.getCodeNew(endpoint);
          masterMap.putAll(returnMap);
        }
        // FIX ME
        Iterable<Edge> endpointInRelationships = roleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_ENDPOINT);
        for (Edge endpointEdge : endpointInRelationships) {
          if (!(Boolean) endpointEdge.getProperty(CommonConstants.ENDPOINT_OWNER)) {
            Vertex endpointNode = endpointEdge.getVertex(com.tinkerpop.blueprints.Direction.IN);
            endpointId = UtilClass.getCodeNew(endpointNode);
          }
        }
      }
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    return endpointId;
  }
  
  private void fillEntityMappingsAndIgnoredKeys(Map<String, Map<String, Object>> entityMappings,
      List<String> ignoredKeys, Map<String, Object> entityFinalMappings)
  {
    for (String key : entityMappings.keySet()) {
      Map<String, Object> attributeMap = entityMappings.get(key);
      if ((boolean) attributeMap.get(IConfigRuleAttributeMappingModel.IS_IGNORED)) {
        ignoredKeys.add(key);
      }
      else {
        entityFinalMappings.put(key,
            attributeMap.get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID));
      }
    }
  }
  
  private static Map<String, List<String>> getAttributeMapping(Vertex profileNode,
      String relationshipLabel, List<String> ignoredKeys)
  {
    Map<String, List<String>> configRuleEntityMappings = new HashMap<>();
    Iterable<Vertex> configRuleNodes = profileNode.getVertices(Direction.OUT, relationshipLabel);
    for (Vertex configRuleNode : configRuleNodes) {
      String mappedElementId = null;
      Map<String, Object> entityMapping = new HashMap<>();
      String columnName = "";
      entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
      if (!(Boolean) entityMapping.get(IConfigRuleAttributeMappingModel.IS_IGNORED)) {
        List<String> columnNames = new ArrayList<>();
        Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Vertex columnMappingNode : columnMappingNodes) {
          columnName = (String) UtilClass.getValueByLanguage(columnMappingNode,
              CommonConstants.LABEL_PROPERTY);
          columnNames.add(columnName);
        }
        
        Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex entityMappingNode : entityMappingNodes) {
          mappedElementId = UtilClass.getCodeNew(entityMappingNode);
        }
        List<String> configRuleEntityMappingsList = null;
        if ((configRuleEntityMappingsList = configRuleEntityMappings.get(columnName)) != null) {
          configRuleEntityMappingsList.add(mappedElementId);
        }
        else {
          configRuleEntityMappings.put(columnName,
              new ArrayList<String>(Arrays.asList(mappedElementId)));
        }
      }
      else {
        ignoredKeys.add(columnName);
      }
    }
    
    return configRuleEntityMappings;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllMappedMappings/*" };
  }
}
