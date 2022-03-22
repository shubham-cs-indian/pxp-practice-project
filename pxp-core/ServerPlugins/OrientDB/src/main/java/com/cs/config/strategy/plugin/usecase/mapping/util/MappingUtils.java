package com.cs.config.strategy.plugin.usecase.mapping.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingADMModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagValueMappingModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings({ "unchecked" })
public class MappingUtils {
  
  public static void getMapFromProfileNode(Vertex profileNode, HashMap<String, Object> returnMap,
      List<String> mappedColumns, Map<String, Object> attributeMapForIdAndLabel,
      Map<String, Object> tagMapForIdAndLabel, Map<String, Object> taxonomyMapForIdAndLabel,
      Map<String, Object> klassMapForIdAndLabel, Map<String, Object> relationshipMapForIdAndLabel) throws Exception
  {
    Map<String, Object> attributeMappings = new HashMap<>();
    Map<String, Object> tagMappings = new HashMap<>();
    attributeMappings
        .putAll(getEntityMapping(profileNode, RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE,
            mappedColumns, attributeMapForIdAndLabel));
    
    Iterable<Vertex> propetiesConfigRuleNodes = profileNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    Iterable<Vertex> contextTagConfigRuleNodes = profileNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
    
    tagMappings.putAll(getTagMapping(propetiesConfigRuleNodes, mappedColumns, tagMapForIdAndLabel));
    tagMappings.putAll(getTagMapping(contextTagConfigRuleNodes, mappedColumns, tagMapForIdAndLabel));
    Map<String, Object> attributeTreeMap = new TreeMap<String, Object>(attributeMappings);
    Map<String, Object> tagTreeMap = new TreeMap<String, Object>(tagMappings);
    returnMap.put(IMappingModel.ATTRIBUTE_MAPPINGS, new ArrayList<>(attributeTreeMap.values()));
    returnMap.put(IMappingModel.TAG_MAPPINGS, new ArrayList<>(tagTreeMap.values()));
    returnMap.put(IMappingModel.CLASS_MAPPINGS,
        new ArrayList<>(new TreeMap<String, Object>(getEntityMapping(profileNode,
            RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE, mappedColumns, klassMapForIdAndLabel))
                .values()));
    returnMap.put(IMappingModel.TAXONOMY_MAPPINGS,
        new ArrayList<>(new TreeMap<String, Object>(
            getEntityMapping(profileNode, RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE,
                mappedColumns, taxonomyMapForIdAndLabel)).values()));
    
    returnMap.put(IMappingModel.RELATIONSHIP_MAPPINGS,
        new ArrayList<>(new TreeMap<String, Object>(
            getEntityMapping(profileNode, RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE,
                mappedColumns, relationshipMapForIdAndLabel)).values()));
  }
  
  public static void getMappings(HashMap<String, Object> returnMap, String profileId,
      Map<String, Object> attributeMapForIdAndLabel, Map<String, Object> tagMapForIdAndLabel,
      Map<String, Object> taxonomyMapForIdAndLabel, Map<String, Object> klassMapForIdAndLabel, Map<String, Object> relationshipMapForIdAndLabel)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex profileNode = UtilClass.getVertexByIndexedId(profileId,
          VertexLabelConstants.PROPERTY_MAPPING);
      returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), profileNode));
      MappingUtils.getMapFromProfileNode(profileNode, returnMap, new ArrayList<>(),
          attributeMapForIdAndLabel, tagMapForIdAndLabel, taxonomyMapForIdAndLabel,
          klassMapForIdAndLabel, relationshipMapForIdAndLabel);
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
  }
  
  public static List<String> getProcesses(Vertex profileNode)
  {
    List<String> processIds = new ArrayList<>();
    Iterable<Vertex> processes = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROFILE_PROCESS_LINK);
    
    for (Vertex processNode : processes) {
      String processId = processNode.getProperty(CommonConstants.CODE_PROPERTY);
      processIds.add(processId);
    }
    
    return processIds;
  }
  
  public static Map<String, Map<String, Object>> getEntityMapping(Vertex profileNode,
      String relationshipLabel, List<String> mappedColumns, Map<String, Object> mapForIdAndLabel)
  {
    int count = 0;
    Map<String, Map<String, Object>> configRuleEntityMappings = new HashMap<>();
    Iterable<Vertex> configRuleNodes = profileNode.getVertices(Direction.OUT, relationshipLabel);
    for (Vertex configRuleNode : configRuleNodes) {
      Map<String, Object> entityMapping = new HashMap<>();
      String columnName = "";
      entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
      List<String> columnNames = new ArrayList<>();
      entityMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, columnNames);
      Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMappingNode : columnMappingNodes) {
        // instead of getting Column name from label we are getting it from
        // "ColumnName" property.
        columnName = (String) columnMappingNode
            .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
        columnNames.add(columnName);
        mappedColumns.add(columnName);
      }
      
      Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.MAPPED_TO_ENTITY);
      for (Vertex entityMappingNode : entityMappingNodes) {
        String id = UtilClass.getCodeNew(entityMappingNode);
        entityMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID, id);
        Map<String, Object> entityDetails = new HashMap<>();
        entityDetails.put(IConfigEntityInformationModel.ID, id);
        entityDetails.put(IConfigEntityInformationModel.LABEL, (String) UtilClass
            .getValueByLanguage(entityMappingNode, CommonConstants.LABEL_PROPERTY));
        entityDetails.put(IConfigEntityInformationModel.CODE,
            entityMappingNode.getProperty(CommonConstants.CODE_PROPERTY));
        mapForIdAndLabel.put(id, entityDetails);
      }
      if (configRuleEntityMappings.get(columnName) != null) {
        columnName += count;
        count++;
      }
      configRuleEntityMappings.put(columnName, entityMapping);
    }
    
    return configRuleEntityMappings;
  }
  
  public static Map<String, Map<String, Object>> getTagMapping(Iterable<Vertex> propetiesConfigRuleNodes,
      List<String> mappedColumns, Map<String, Object> tagMapForIdAndLabel) throws Exception
  {
    Map<String, Map<String, Object>> configRuleTagMappings = new HashMap<>();
    for(Vertex propertyConfigRuleNode : propetiesConfigRuleNodes) {
    
      Iterable<Vertex> configRuleNodes = propertyConfigRuleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
    for (Vertex configRuleNode : configRuleNodes) {
      List<Map<String, Object>> tagValuesMappingList = new ArrayList<>();
      Map<String, Object> tagMapping = new HashMap<>();
      String columnName = "";
      tagMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
      List<String> columnNames = new ArrayList<>();
      tagMapping.put(IConfigRuleTagMappingModel.COLUMN_NAMES, columnNames);
      Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMappingNode : columnMappingNodes) {
        Map<String, Object> tagValuesMapping = new HashMap<>();
        // instead of getting Column name from label we are getting it from
        // "ColumnName" property.
        columnName = (String) columnMappingNode
            .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
        columnNames.add(columnName);
        mappedColumns.add(columnName);
        Iterable<Vertex> valueNodes = columnMappingNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_VALUE_MAPPING);
        List<Map<String, Object>> tagValueMappingList = new ArrayList<>();
        for (Vertex valueNode : valueNodes) {
          Map<String, Object> tagValueMapping = new HashMap<>();
          Iterator<Vertex> tagValueNodes = valueNode
              .getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY)
              .iterator();
          while (tagValueNodes.hasNext()) {
            Vertex tagValueNode = tagValueNodes.next();
            String id = UtilClass.getCodeNew(valueNode);
            tagValueMapping.put(ITagValueMappingModel.ID, id);
            // instead of getting Tag Value from label we are getting it from
            // "ColumnName"
            // property.
            tagValueMapping.put(ITagValueMappingModel.TAG_VALUE,
                (String) valueNode.getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME));
            tagValueMapping.put(ITagValueMappingModel.IS_IGNORE_CASE,
                valueNode.getProperty(ITagValueMappingModel.IS_IGNORE_CASE));
            tagValueMapping.put(ITagValueMappingModel.MAPPED_TAG_VALUE_ID,
                UtilClass.getCodeNew(tagValueNode));
            // tagMapForIdAndLabel.put(id, (String)
            // UtilClass.getValueByLanguage(tagValueNode,
            // CommonConstants.LABEL_PROPERTY));
            tagValueMapping.put(ITagValueMappingModel.IS_AUTOMAPPED, false);
            tagValueMappingList.add(tagValueMapping);
          }
        }
        if (tagValueMappingList.size() > 0) {
          tagValuesMapping.put(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
          tagValuesMapping.put(IColumnValueTagValueMappingModel.MAPPINGS, tagValueMappingList);
          tagValuesMappingList.add(tagValuesMapping);
        }
      }
      
      Iterable<Vertex> tagGroupMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.MAPPED_TO_ENTITY);
      for (Vertex tagGroupMappingNode : tagGroupMappingNodes) {
        String id = UtilClass.getCodeNew(tagGroupMappingNode);
        tagMapping.put(IConfigRuleTagMappingModel.MAPPED_ELEMENT_ID, id);
        Map<String, Object> entity = TagUtils.getTagMap(tagGroupMappingNode, false);
        tagMapForIdAndLabel.put(id, entity);
        // tagMapForIdAndLabel.put(id, (String)
        // UtilClass.getValueByLanguage(tagGroupMappingNode,
        // CommonConstants.LABEL_PROPERTY));
      }
      tagMapping.put(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS, tagValuesMappingList);
      configRuleTagMappings.put(columnName, tagMapping);
    }
  }
    
    return configRuleTagMappings;
  }
  
  public static void deleteEndpointMappings(Vertex profileNode)
  {
    Iterable<Vertex> attributeConfigRules = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
    for (Vertex attributeConfigRule : attributeConfigRules) {
      Iterable<Vertex> columnMappings = attributeConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        columnMapping.remove();
      }
      attributeConfigRule.remove();
    }
    
    Iterable<Vertex> tagConfigRules = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
    for (Vertex tagConfigRule : tagConfigRules) {
      Iterable<Vertex> columnMappings = tagConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        Iterable<Vertex> tagValues = columnMapping.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_VALUE_MAPPING);
        for (Vertex tagValue : tagValues) {
          tagValue.remove();
        }
        columnMapping.remove();
      }
      tagConfigRule.remove();
    }
  }
  
  public static void saveMapping(Vertex profileNode, HashMap<String, Object> profileMap)
      throws Exception
  {
    List<Map<String, Object>> addedAttributesMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.ADDED_ATRRIBUTE_MAPPINGS);
    List<Map<String, Object>> modifiedAttributesMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.MODIFIED_ATTRIBUTE_MAPPINGS);
    List<String> deletedAttributesMapping = (List<String>) profileMap
        .get(ISaveMappingModel.DELETED_ATTRIBUTE_MAPPINGS);
    saveAttributeMapping(profileNode, addedAttributesMapping, modifiedAttributesMapping,
        deletedAttributesMapping);
    
    List<Map<String, Object>> addedTagsMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.ADDED_TAG_MAPPINGS);
    List<Map<String, Object>> modifiedTagsMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.MODIFIED_TAG_MAPPINGS);
    List<String> deletedTagsMapping = (List<String>) profileMap
        .get(ISaveMappingModel.DELETED_TAG_MAPPINGS);
    saveTagMapping(profileNode, addedTagsMapping, modifiedTagsMapping, deletedTagsMapping);
    
    List<Map<String, Object>> addedClassesMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.ADDED_CLASS_MAPPINGS);
    List<Map<String, Object>> modifiedClassesMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.MODIFIED_CLASS_MAPPINGS);
    List<String> deletedClassesMapping = (List<String>) profileMap
        .get(ISaveMappingModel.DELETED_CLASS_MAPPINGS);
    saveClassMapping(profileNode, addedClassesMapping, modifiedClassesMapping,
        deletedClassesMapping);
    
    List<Map<String, Object>> addedTaxonomiesMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.ADDED_TAXONOMY_MAPPINGS);
    List<Map<String, Object>> modifiedTaxonomiesMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.MODIFIED_TAXONOMY_MAPPINGS);
    List<String> deletedTaxonomiesMapping = (List<String>) profileMap
        .get(ISaveMappingModel.DELETED_TAXONOMY_MAPPINGS);
    saveTaxonomyMapping(profileNode, addedTaxonomiesMapping, modifiedTaxonomiesMapping,
        deletedTaxonomiesMapping);
    
    List<Map<String, Object>> addedRelationshipsMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.ADDED_RELATIONSHIP_MAPPINGS);
    List<Map<String, Object>> modifiedRelationshipsMapping = (List<Map<String, Object>>) profileMap
        .get(ISaveMappingModel.MODIFIED_RELATIONSHIP_MAPPINGS);
    List<String> deletedRelationshipsMapping = (List<String>) profileMap
        .get(ISaveMappingModel.DELETED_RELATIONSHIP_MAPPINGS);
    saveRelationshipMapping(profileNode, addedRelationshipsMapping, modifiedRelationshipsMapping,
        deletedRelationshipsMapping);
  }
  
  public static void saveTagMapping(Vertex profileNode, List<Map<String, Object>> addedTagsMapping,
      List<Map<String, Object>> modifiedTagsMapping, List<String> deletedTagsMapping)
      throws Exception
  {
    addTagMappings(profileNode, addedTagsMapping);
    
    modifyTagMappings(profileNode, modifiedTagsMapping);
    
    deleteTagMappings(deletedTagsMapping);
  }
  
  private static void addTagMappings(Vertex profileNode, List<Map<String, Object>> addedTagsMapping)
      throws Exception
  {
    for (Map<String, Object> addedTagMapping : addedTagsMapping) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.CONFIG_RULE, CommonConstants.CODE_PROPERTY);
      Vertex configRuleNode = UtilClass.createNode(addedTagMapping, vertexType,
          Arrays.asList(IConfigRuleTagMappingModel.COLUMN_NAMES,
              IConfigRuleTagMappingModel.MAPPED_ELEMENT_ID,
              IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS));
      profileNode.addEdge(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, configRuleNode);
      for (String columnName : (List<String>) addedTagMapping
          .get(IConfigRuleAttributeMappingModel.COLUMN_NAMES)) {
        vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
            CommonConstants.CODE_PROPERTY);
        Map<String, Object> columnMappingMap = new HashMap<>();
        // instead of storing Column name in label we are storing "ColumnName"
        // property.
        // columnMappingMap.put(IConfigEntityInformationModel.LABEL,
        // columnName);
        columnMappingMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
        Vertex columnMappingNode = UtilClass.createNode(columnMappingMap, vertexType,
            Arrays.asList(IConfigRuleAttributeMappingModel.COLUMN_NAMES,
                IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID));
        configRuleNode.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING, columnMappingNode);
        List<Map<String, Object>> tagValueMappingList = (List<Map<String, Object>>) addedTagMapping
            .get(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS);
        if (tagValueMappingList != null && tagValueMappingList.size() > 0) {
          for (Map<String, Object> tagValueMapping : tagValueMappingList) {
            vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.VALUE,
                CommonConstants.CODE_PROPERTY);
            if (((String) tagValueMapping.get(IColumnValueTagValueMappingModel.COLUMN_NAME))
                .equals(columnName)) {
              List<Map<String, Object>> tvMappingList = (List<Map<String, Object>>) tagValueMapping
                  .get(IColumnValueTagValueMappingModel.MAPPINGS);
              for (Map<String, Object> tvMapping : tvMappingList) {
                Map<String, Object> tagValueMap = new HashMap<>();
                tagValueMap.put(IConfigEntityInformationModel.ID,
                    tvMapping.get(ITagValueMappingModel.ID));
                /*instead of storing Column name in label we are storing in "ColumnName" property.
                tagValueMap.put(IConfigEntityInformationModel.LABEL,
                    tvMapping.get(ITagValueMappingModel.TAG_VALUE));*/
                tagValueMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME,
                    tvMapping.get(ITagValueMappingModel.TAG_VALUE));
                tagValueMap.put(ITagValueMappingModel.IS_AUTOMAPPED,
                    tvMapping.get(ITagValueMappingModel.IS_AUTOMAPPED));
                tagValueMap.put(ITagValueMappingModel.IS_IGNORE_CASE,
                    tvMapping.get(ITagValueMappingModel.IS_IGNORE_CASE));
                Vertex tagValueNode = UtilClass.createNode(tagValueMap, vertexType,
                    new ArrayList<>());
                columnMappingNode.addEdge(RelationshipLabelConstants.HAS_VALUE_MAPPING,
                    tagValueNode);
                Vertex tagGroupTagValueNode = UtilClass.getVertexById(
                    (String) tvMapping.get(ITagValueMappingModel.MAPPED_TAG_VALUE_ID),
                    VertexLabelConstants.ENTITY_TAG);
                tagValueNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY,
                    tagGroupTagValueNode);
              }
            }
          }
        }
      }
      String tagGroupId = (String) addedTagMapping
          .get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID);
      if (tagGroupId != null) {
        Vertex tagGroupNode = UtilClass.getVertexById(tagGroupId, VertexLabelConstants.ENTITY_TAG);
        configRuleNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, tagGroupNode);
      }
    }
  }
  
  private static void modifyTagMappings(Vertex profileNode,
      List<Map<String, Object>> modifiedTagsMapping) throws Exception
  {
    for (Map<String, Object> modifiedTagMapping : modifiedTagsMapping) {
      Vertex configRuleNode = UtilClass.getVertexById(
          (String) modifiedTagMapping.get(IConfigRuleTagMappingModel.ID),
          VertexLabelConstants.CONFIG_RULE);
      configRuleNode.setProperty(IConfigRuleTagMappingModel.IS_IGNORED,
          modifiedTagMapping.get(IConfigRuleTagMappingModel.IS_IGNORED));
      for (String columnName : (List<String>) modifiedTagMapping
          .get(IConfigRuleAttributeMappingModel.COLUMN_NAMES)) {
        Iterator<Vertex> columnMappingNodes = configRuleNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_COLUMN_MAPPING)
            .iterator();
        Vertex columnMappingNode = columnMappingNodes.next();
        // instead of storing Column name in label we are storing in
        // "ColumnName" property.
        // columnMappingNode.setProperty(EntityUtil.getLanguageConvertedField(IConfigEntityInformationModel.LABEL),
        // columnName);
        columnMappingNode.setProperty(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
        List<Map<String, Object>> tagValueMappingList = (List<Map<String, Object>>) modifiedTagMapping
            .get(IConfigRuleTagMappingADMModel.ADDED_TAG_VALUE_MAPPINGS);
        if (tagValueMappingList != null && tagValueMappingList.size() > 0) {
          for (Map<String, Object> tagValueMapping : tagValueMappingList) {
            Map<String, Object> tagValueMap = new HashMap<>();
            OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
                VertexLabelConstants.VALUE, CommonConstants.CODE_PROPERTY);
            tagValueMap.put(IConfigEntityInformationModel.ID,
                tagValueMapping.get(ITagValueMappingModel.ID));
            // instead of storing Column name in label we are storing in
            // "ColumnName" property.
            /*tagValueMap.put(EntityUtil.getLanguageConvertedField(IConfigEntityInformationModel.LABEL),
            tagValueMapping.get(ITagValueMappingModel.TAG_VALUE));*/
            tagValueMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME,
                tagValueMapping.get(ITagValueMappingModel.TAG_VALUE));
            tagValueMap.put(ITagValueMappingModel.IS_IGNORE_CASE,
                tagValueMapping.get(ITagValueMappingModel.IS_IGNORE_CASE));
            Vertex valueNode = UtilClass.createNode(tagValueMap, vertexType, new ArrayList<>());
            columnMappingNode.addEdge(RelationshipLabelConstants.HAS_VALUE_MAPPING, valueNode);
            Vertex tagGroupTagValueNode = UtilClass.getVertexById(
                (String) tagValueMapping.get(ITagValueMappingModel.MAPPED_TAG_VALUE_ID),
                VertexLabelConstants.ENTITY_TAG);
            valueNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, tagGroupTagValueNode);
          }
        }
        
        List<Map<String, Object>> modifiedTagValueMappingList = (List<Map<String, Object>>) modifiedTagMapping
            .get(IConfigRuleTagMappingADMModel.MODIFIED_TAG_VALUE_MAPPINGS);
        if (modifiedTagValueMappingList != null && modifiedTagValueMappingList.size() > 0) {
          for (Map<String, Object> modifiedTagValueMapping : modifiedTagValueMappingList) {
            Vertex valueNode = UtilClass.getVertexById(
                (String) modifiedTagValueMapping.get(ITagValueMappingModel.ID),
                VertexLabelConstants.VALUE);
            // instead of storing Column name in label we are storing in
            // "ColumnName" property.
            /*valueNode.setProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY),
            modifiedTagValueMapping.get(ITagValueMappingModel.TAG_VALUE));*/
            valueNode.setProperty(IColumnValueTagValueMappingModel.COLUMN_NAME,
                modifiedTagValueMapping.get(ITagValueMappingModel.TAG_VALUE));
            valueNode.setProperty(ITagValueMappingModel.IS_IGNORE_CASE,
                modifiedTagValueMapping.get(ITagValueMappingModel.IS_IGNORE_CASE));
            
            String tagValueId = (String) modifiedTagValueMapping
                .get(ITagValueMappingModel.MAPPED_TAG_VALUE_ID);
            Vertex tagValueNode = UtilClass.getVertexById(tagValueId,
                VertexLabelConstants.ENTITY_TAG);
            Iterator<Edge> iterator = valueNode
                .getEdges(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY)
                .iterator();
            if (iterator.hasNext()) {
              Edge mappedToEntity = iterator.next();
              mappedToEntity.remove();
            }
            valueNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, tagValueNode);
          }
        }
        
        List<String> deletedTagValueMappingList = (List<String>) modifiedTagMapping
            .get(IConfigRuleTagMappingADMModel.DELETED_TAG_VALUE_MAPPINGS);
        if (deletedTagValueMappingList != null && deletedTagValueMappingList.size() > 0) {
          for (String deletedTagValueMappingId : deletedTagValueMappingList) {
            Vertex valueNode = UtilClass.getVertexById(deletedTagValueMappingId,
                VertexLabelConstants.VALUE);
            valueNode.remove();
          }
        }
      }
    }
  }
  
  private static void deleteTagMappings(List<String> deletedTagsMapping) throws Exception
  {
    for (String deletedTagMappingId : deletedTagsMapping) {
      Vertex tagConfigRule = UtilClass.getVertexById(deletedTagMappingId,
          VertexLabelConstants.CONFIG_RULE);
      Iterable<Vertex> columnMappings = tagConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        Iterable<Vertex> tagValues = columnMapping.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_VALUE_MAPPING);
        for (Vertex tagValue : tagValues) {
          tagValue.remove();
        }
        columnMapping.remove();
      }
      tagConfigRule.remove();
    }
  }
  
  public static void saveAttributeMapping(Vertex profileNode,
      List<Map<String, Object>> addedAttributesMapping,
      List<Map<String, Object>> modifiedAttributesMapping, List<String> deletedAttributesMapping)
      throws Exception
  {
    addEntityMappings(profileNode, addedAttributesMapping,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
    modifyEntityMappings(profileNode, modifiedAttributesMapping);
    deleteEntityMappings(deletedAttributesMapping);
  }
  
  private static void deleteEntityMappings(List<String> deletedEntitiesMapping) throws Exception
  {
    for (String deletedEntityMappingId : deletedEntitiesMapping) {
      Vertex entityConfigRule = UtilClass.getVertexById(deletedEntityMappingId,
          VertexLabelConstants.CONFIG_RULE);
      Iterable<Vertex> columnMappings = entityConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        columnMapping.remove();
      }
      entityConfigRule.remove();
    }
  }
  
  private static void addEntityMappings(Vertex profileNode,
      List<Map<String, Object>> addedEntitiesMapping, String entityType, String relationshipLabel)
      throws Exception
  {
    for (Map<String, Object> addedEntityMapping : addedEntitiesMapping) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.CONFIG_RULE, CommonConstants.CODE_PROPERTY);
      Vertex configRuleNode = UtilClass.createNode(addedEntityMapping, vertexType,
          Arrays.asList(IConfigRuleAttributeMappingModel.COLUMN_NAMES,
              IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID));
      profileNode.addEdge(relationshipLabel, configRuleNode);
      for (String columnName : (List<String>) addedEntityMapping
          .get(IConfigRuleAttributeMappingModel.COLUMN_NAMES)) {
        vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
            CommonConstants.CODE_PROPERTY);
        Map<String, Object> columnMappingMap = new HashMap<>();
        // instead of storing Column name in label we are storing in
        // "ColumnName" property.
        // columnMappingMap.put(IConfigEntityInformationModel.LABEL,
        // columnName);
        columnMappingMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
        Vertex columnMappingNode = UtilClass.createNode(columnMappingMap, vertexType,
            Arrays.asList(IConfigRuleAttributeMappingModel.COLUMN_NAMES,
                IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID));
        configRuleNode.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING, columnMappingNode);
      }
      String entityId = (String) addedEntityMapping
          .get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID);
      if (entityId != null) {
        Vertex attributeNode = UtilClass.getVertexById(entityId, entityType);
        configRuleNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, attributeNode);
      }
    }
  }
  
  public static void modifyEntityMappings(Vertex profileNode,
      List<Map<String, Object>> modifiedEntitiesMapping) throws Exception
  {
    for (Map<String, Object> modifiedEntityMapping : modifiedEntitiesMapping) {
      Vertex configRuleNode = UtilClass.getVertexById(
          (String) modifiedEntityMapping.get(IConfigRuleTagMappingModel.ID),
          VertexLabelConstants.CONFIG_RULE);
      configRuleNode.setProperty(IConfigRuleTagMappingModel.IS_IGNORED,
          modifiedEntityMapping.get(IConfigRuleTagMappingModel.IS_IGNORED));
      for (String columnName : (List<String>) modifiedEntityMapping
          .get(IConfigRuleAttributeMappingModel.COLUMN_NAMES)) {
        Iterator<Vertex> columnMappingNodes = configRuleNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_COLUMN_MAPPING)
            .iterator();
        Vertex columnMappingNode = columnMappingNodes.next();
        // instead of storing Column name in label we are storing in
        // "ColumnName" property.
        // columnMappingNode.setProperty(EntityUtil.getLanguageConvertedField(IConfigEntityInformationModel.LABEL),
        // columnName);
        columnMappingNode.setProperty(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
      }
    }
  }
  
  public static void saveClassMapping(Vertex profileNode,
      List<Map<String, Object>> addedClassesMapping,
      List<Map<String, Object>> modifiedClassesMapping, List<String> deletedClassesMapping)
      throws Exception
  {
    addEntityMappings(profileNode, addedClassesMapping, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
        RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE);
    modifyEntityMappings(profileNode, modifiedClassesMapping);
    deleteEntityMappings(deletedClassesMapping);
  }
  
  public static void saveTaxonomyMapping(Vertex profileNode,
      List<Map<String, Object>> addedTaxonomiesMapping,
      List<Map<String, Object>> modifiedTaxonomiesMapping, List<String> deletedTaxonomiesMapping)
      throws Exception
  {
    addEntityMappings(profileNode, addedTaxonomiesMapping, VertexLabelConstants.ROOT_KLASS_TAXONOMY,
        RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE);
    modifyEntityMappings(profileNode, modifiedTaxonomiesMapping);
    deleteEntityMappings(deletedTaxonomiesMapping);
  }

  public static void saveRelationshipMapping(Vertex profileNode,
      List<Map<String, Object>> addedRelationshipsMapping,
      List<Map<String, Object>> modifiedRelationshipsMapping, List<String> deletedRelationshipsMapping)
      throws Exception
  {
    addEntityMappings(profileNode, addedRelationshipsMapping, VertexLabelConstants.ROOT_RELATIONSHIP,
        RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE);
    modifyEntityMappings(profileNode, modifiedRelationshipsMapping);
    deleteEntityMappings(deletedRelationshipsMapping);
  }
 
  public static void deleteEntityMappings(Vertex profileNode, String relationshipLabel)
  {
    Iterable<Vertex> configRules = profileNode.getVertices(Direction.OUT, relationshipLabel);
    for (Vertex configRule : configRules) {
      Iterable<Vertex> columnMappings = configRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        columnMapping.remove();
      }
      configRule.remove();
    }
  }
}
