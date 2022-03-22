package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.endpoint.IGetMappedEndpointRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetMappingForImport extends AbstractOrientPlugin {
  
  public GetMappingForImport(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMappingForImport/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> attributesFinalMappings = new HashMap<>();
    Map<String, Object> tagsFinalMappings = new HashMap<>();
    
    Map<String, Object> attributesMappedMappings = new HashMap<>();
    Map<String, Object> tagsMappedMappings = new HashMap<>();
    Map<String, Object> classMappedMappings = new HashMap<>();
    Map<String, Object> taxonomyMappedMappings = new HashMap<>();
    Map<String, Object> relationshipMappedMappings = new HashMap<>();
    List<String> ignoredKeys = new ArrayList<>();
    
    List<String> fileHeaders = (List<String>) requestMap
        .get(IGetMappedEndpointRequestModel.FILE_HEADERS);
    String mappingId = (String) requestMap.get(IGetMappingForImportRequestModel.MAPPING_ID);
    
    Map<String, List<String>> attributeMappings = new HashMap<>();
    Map<String, Map<String, Object>> tagMappings = new HashMap<>();
    Map<String, Map<String, Object>> classMappings = new HashMap<>();
    Map<String, Map<String, Object>> taxonomyMappings = new HashMap<>();
    Map<String, Map<String, Object>> relationshipMappings = new HashMap<>();
    Map<String, Object> configTagMappings = new HashMap<>();
    
    fillAttributeAndTagMappings(mappingId, attributeMappings, tagMappings, classMappings,
        taxonomyMappings, ignoredKeys, configTagMappings, relationshipMappings);
    
    fillEntityMappingsAndIgnoredKeysForAttributes(attributeMappings, ignoredKeys,
        attributesMappedMappings);
    fillEntityMappingsAndIgnoredKeys(tagMappings, ignoredKeys, tagsMappedMappings);
    fillEntityMappingsAndIgnoredKeys(classMappings, ignoredKeys, classMappedMappings);
    fillEntityMappingsAndIgnoredKeys(taxonomyMappings, ignoredKeys, taxonomyMappedMappings);
    fillEntityMappingsAndIgnoredKeys(relationshipMappings, ignoredKeys, relationshipMappedMappings);
    
    List<String> entitiesToSearch = new ArrayList<>();
    for (String header : fileHeaders) {
      if (!ignoredKeys.contains(header)) {
        if (attributesMappedMappings.containsKey(header)) {
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
            + CommonConstants.CODE_PROPERTY + " in " + entitiesToSearch + " and @Class ="
            + VertexLabelConstants.ENTITY_TAG))
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
    Vertex mappingNode = UtilClass.getVertexById(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
    Map<String, Object> configTagTreeMap = new TreeMap<String, Object>(configTagMappings);    
    returnMap.put(IGetMappingForImportResponseModel.MAPPING_TYPE, mappingNode.getProperty(IGetMappingForImportResponseModel.MAPPING_TYPE));
    returnMap.put(IGetMappingForImportResponseModel.ATTRIBUTES, attributesFinalMappings);
    returnMap.put(IGetMappingForImportResponseModel.TAGS, tagsFinalMappings);
    returnMap.put(IGetMappingForImportResponseModel.TAXONOMIES, taxonomyMappedMappings);
    returnMap.put(IGetMappingForImportResponseModel.CLASSES, classMappedMappings);
    returnMap.put(IGetMappingForImportResponseModel.HEADERS_TO_READ, validHeadersList);
    returnMap.put(IGetMappingForImportResponseModel.ENDPOINT_ID, mappingId);
    returnMap.put(IGetMappingForImportResponseModel.TAG_MAPPINGS,  new ArrayList<>(configTagTreeMap.values()));
    returnMap.put(IGetMappingForImportResponseModel.RELATIONSHIPS, relationshipMappedMappings);
    return returnMap;
  }
  
  private void fillAttributeAndTagMappings(String mappingId,
      Map<String, List<String>> attributeMap, Map<String, Map<String, Object>> tagMap,
      Map<String, Map<String, Object>> classMappings,
      Map<String, Map<String, Object>> taxonomyMappings, List<String> ignoredKeys,
      Map<String, Object> configTagMappings,  Map<String, Map<String, Object>> relationshipMappings) throws Exception
  {
    Vertex mappingNode = UtilClass.getVertexById(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
    HashMap<String, Object> masterMap = new HashMap<>();
    HashMap<String, Object> returnMap = new HashMap<>();
    attributeMap.putAll(getAttributeMapping(mappingNode,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, ignoredKeys));
    
    Iterable<Vertex> propetiesConfigRuleNodes = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    Iterable<Vertex> contextTagConfigRuleNodes = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
    
    tagMap.putAll(EndpointUtils.getTagMapping(propetiesConfigRuleNodes, new ArrayList<>()));
    tagMap.putAll(EndpointUtils.getTagMapping(contextTagConfigRuleNodes, new ArrayList<>()));
    
    classMappings.putAll(EndpointUtils.getEntityMapping(mappingNode,
        RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE, new ArrayList<>()));
    taxonomyMappings.putAll(EndpointUtils.getEntityMapping(mappingNode,
        RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE, new ArrayList<>()));
    relationshipMappings.putAll(EndpointUtils.getEntityMapping(mappingNode,
        RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE, new ArrayList<>()));
    masterMap.putAll(returnMap);
    
    configTagMappings.putAll(MappingUtils.getTagMapping(propetiesConfigRuleNodes, new ArrayList<String>(),
        new HashMap<String, Object>()));
    configTagMappings.putAll(MappingUtils.getTagMapping(contextTagConfigRuleNodes, new ArrayList<String>(),
        new HashMap<String, Object>()));
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
  
  private void fillEntityMappingsAndIgnoredKeysForAttributes(
      Map<String, List<String>> entityMappings, List<String> ignoredKeys,
      Map<String, Object> entityFinalMappings)
  {
    for (String key : entityMappings.keySet()) {
      entityFinalMappings.put(key, entityMappings.get(key));
    }
  }
  
  private static Map<String, List<String>> getAttributeMapping(Vertex mappingNode,
      String relationshipLabel, List<String> ignoredKeys)
  {
    // int count = 0;
    Map<String, List<String>> configRuleEntityMappings = new HashMap<>();
      Iterable<Vertex> propetiesConfigRuleNodes = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
      for(Vertex propertyConfigRuleNode : propetiesConfigRuleNodes) {
        Iterable<Vertex> configRuleNodes = propertyConfigRuleNode.getVertices(Direction.OUT, relationshipLabel);
      for (Vertex configRuleNode : configRuleNodes) {
        String mappedElementId = null;
        Map<String, Object> entityMapping = new HashMap<>();
        String columnName = "";
        entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
        
        Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Vertex columnMappingNode : columnMappingNodes) {
          // instead of getting Column name from label we are getting it from
          // "ColumnName"
          // property.
          columnName = columnMappingNode.getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
        }
        if (!(Boolean) entityMapping.get(IConfigRuleAttributeMappingModel.IS_IGNORED)) {
          
          Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
              RelationshipLabelConstants.MAPPED_TO_ENTITY);
          for (Vertex entityMappingNode : entityMappingNodes) {
            mappedElementId = UtilClass.getCodeNew(entityMappingNode);
          }
          if (configRuleEntityMappings.containsKey(columnName)) {
            List<String> attributeIds = configRuleEntityMappings.get(columnName);
            if (attributeIds == null) {
              attributeIds = new ArrayList<>();
            }
            if (!attributeIds.contains(mappedElementId)) {
              attributeIds.add(mappedElementId);
            }
            configRuleEntityMappings.put(columnName, attributeIds);
            /*columnName += count;
            count++;*/
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
  }
    
    return configRuleEntityMappings;
  }
}
