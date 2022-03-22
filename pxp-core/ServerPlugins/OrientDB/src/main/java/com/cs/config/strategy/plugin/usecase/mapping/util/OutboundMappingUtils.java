package com.cs.config.strategy.plugin.usecase.mapping.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cs.config.strategy.plugin.model.mapping.IMappingConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.mapping.GetPropertyGroupInfo;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagInfoModel;
import com.cs.core.config.interactor.model.mapping.ITagValueMappingModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class OutboundMappingUtils {
  
  public static final List<String> FIELDS_TO_FETCH = Arrays.asList(CommonConstants.CID_PROPERTY, IAttribute.LABEL, IAttribute.ICON,
      IAttribute.CODE, IAttribute.TYPE);
  
  /**
   * @param returnMap
   * @param mappingId
   * @param mappingHelperModel
   * @throws Exception
   */
  public static void getMappings(Map<String, Object> returnMap, String mappingId,
      IMappingHelperModel mappingHelperModel) throws Exception
  {
    try {
      Vertex mappingNode = UtilClass.getVertexByIndexedId(mappingId,
          VertexLabelConstants.PROPERTY_MAPPING);
      returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), mappingNode));
      getMapFromProfileNode(mappingNode, returnMap, mappingHelperModel);
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
  }
  
  /**
   * @param mappingNode
   * @param returnMap
   * @param mappingHelperModel
   * @throws Exception
   */
  private static void getMapFromProfileNode(Vertex mappingNode, Map<String, Object> returnMap,
      IMappingHelperModel mappingHelperModel) throws Exception
  {
    switch (mappingHelperModel.getTabId()) {
      case ProcessConstants.KLASS_TAB_ID:
        returnMap.put(IMappingModel.CLASS_MAPPINGS,
            new ArrayList<>(new TreeMap<String, Object>(
                getEntityMapping(mappingNode, RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE,
                    mappingHelperModel.getConfigDetails()
                        .getKlasses())).values()));
        break;
      case ProcessConstants.TAXONOMY_TAB_ID:
        returnMap
            .put(IMappingModel.TAXONOMY_MAPPINGS,
                new ArrayList<>(new TreeMap<String, Object>(getEntityMapping(mappingNode,
                    RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE,
                    mappingHelperModel.getConfigDetails()
                        .getTaxonomy())).values()));
        break;
      case ProcessConstants.PROPERTYCOLLECTION_TAB_ID:
        getPropertiesMapping(mappingNode, mappingHelperModel, returnMap);
        break;
      case ProcessConstants.RELATIONSHIP_TAB_ID:
        returnMap
        .put(IMappingModel.RELATIONSHIP_MAPPINGS,
            new ArrayList<>(new TreeMap<String, Object>(getEntityMapping(mappingNode,
                RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE,
                mappingHelperModel.getConfigDetails()
                    .getRelationships())).values()));
        break;
      case ProcessConstants.CONTEXT_TAG_TAB_ID:
        getContextTagMapping(mappingNode, mappingHelperModel, returnMap);
        break;
      default:
        break;
    }
  }
  
  /**
   * @param mappingNode
   * @param mappingHelperModel
   * @param returnMap
   * @throws Exception
   */
  private static void getPropertiesMapping(Vertex mappingNode,
      IMappingHelperModel mappingHelperModel, Map<String, Object> returnMap) throws Exception
  {
    // TODO Optimize get of PCI node.
    List<String> linkedPropertyCollectionIds = getLinkedPropertyCollectionIds(mappingNode, mappingHelperModel.getConfigDetails()
        .getPropertyCollections(), RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE, VertexLabelConstants.PROPERTY_COLLECTION);
    returnMap.put(IOutBoundMappingModel.PROPERTY_COLLECTION_IDS,
        linkedPropertyCollectionIds);
    /*if (mappingHelperModel.getSelectedPropertyCollectionId() == null
        && !mappingHelperModel.getIsExport()) {
      return;
    }*/
    Map<String, Object> attributeMappings = new HashMap<>();
    Map<String, Object> tagMappings = new HashMap<>();
    if (mappingHelperModel.getIsExport()) {
      Iterable<Vertex> pciNodes = mappingNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
      for (Vertex pciNode : pciNodes) {
        getPCMappings(mappingNode, mappingHelperModel, pciNode, attributeMappings, tagMappings);
      }
    }
    else {
      if (mappingHelperModel.getSelectedPropertyCollectionId() != null) {
        Vertex pciNode = getPCINode(mappingNode, mappingHelperModel.getSelectedPropertyCollectionId(),
            RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
        getPCMappings(mappingNode, mappingHelperModel, pciNode, attributeMappings, tagMappings);
      }
      else {
        for (String pcId : linkedPropertyCollectionIds) {
          Vertex propertyCollection = getPCINode(mappingNode, pcId, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
          getPCMappings(mappingNode, mappingHelperModel, propertyCollection, attributeMappings, tagMappings);
        }
      }
    }
    Map<String, Object> attributeTreeMap = new TreeMap<String, Object>(attributeMappings);
    Map<String, Object> tagTreeMap = new TreeMap<String, Object>(tagMappings);
    returnMap.put(IMappingModel.ATTRIBUTE_MAPPINGS, new ArrayList<>(attributeTreeMap.values()));
    returnMap.put(IMappingModel.TAG_MAPPINGS, new ArrayList<>(tagTreeMap.values()));
  }
  
  private static void getContextTagMapping(Vertex mappingNode,
      IMappingHelperModel mappingHelperModel, Map<String, Object> returnMap) throws Exception
  {
    List<String> linkedContextIds = getLinkedPropertyCollectionIds(mappingNode,
        mappingHelperModel.getConfigDetails().getContexts(), RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE,
        VertexLabelConstants.VARIANT_CONTEXT);
    returnMap.put(IOutBoundMappingModel.CONTEXT_IDS, linkedContextIds);
   
    Map<String, Object> tagMappings = new HashMap<>();
    if (mappingHelperModel.getIsExport()) {
      Iterable<Vertex> contextCRNodes = mappingNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
      for (Vertex contextCRNode : contextCRNodes) {
        tagMappings.putAll(getTagMapping(mappingNode, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
            mappingHelperModel.getConfigDetails()
                .getTags(),
                contextCRNode, new ArrayList<String>()));
      }
    }
    else {
      if (mappingHelperModel.getSelectedContextId() != null) {
        Vertex contextCRNode = getPCINode(mappingNode, mappingHelperModel.getSelectedContextId(),
            RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
        tagMappings.putAll(getTagMapping(mappingNode, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
            mappingHelperModel.getConfigDetails().getTags(), contextCRNode, new ArrayList<String>()));
      }
      else {
        for (String contextId : linkedContextIds) {
          Vertex contextCRNode = getPCINode(mappingNode, contextId, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
          tagMappings.putAll(getTagMapping(mappingNode, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
              mappingHelperModel.getConfigDetails().getTags(), contextCRNode, new ArrayList<String>()));
        }
      }
    }
    
    Map<String, Object> tagTreeMap = new TreeMap<String, Object>(tagMappings);
    
    Collection<Object> tagMap =(Collection<Object>) returnMap.get(IMappingModel.TAG_MAPPINGS);
    if (tagMap != null && !tagMap.isEmpty()) {      
      tagMap.addAll(tagTreeMap.values());
      returnMap.put(IMappingModel.TAG_MAPPINGS, tagMap);
    }
    else {
      returnMap.put(IMappingModel.TAG_MAPPINGS, new ArrayList<>(tagTreeMap.values()));
    }
  }
  
  public static void getPCMappings(Vertex mappingNode, IMappingHelperModel mappingHelperModel,
      Vertex pciNode, Map<String, Object> attributeMappings, Map<String, Object> tagMappings)
      throws Exception
  {
    attributeMappings.putAll(getAttributeMapping(mappingNode,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, mappingHelperModel.getConfigDetails()
            .getAttributes(),
        pciNode, new ArrayList<String>()));
    tagMappings.putAll(getTagMapping(mappingNode, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
        mappingHelperModel.getConfigDetails()
            .getTags(),
        pciNode, new ArrayList<String>()));
  }
  
  /**
   * @param mappingNode
   * @param selectedEntityId
   * @return
   */
  private static Vertex getPCINode(Vertex mappingNode, String selectedEntityId, String relationsipLabel)
  {
    Iterable<Vertex> pciNodes = mappingNode.getVertices(Direction.OUT,
        relationsipLabel);
    for (Vertex pciNode : pciNodes) {
      Iterable<Vertex> pcNodes = pciNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.MAPPED_TO_ENTITY);
      for (Vertex pcNode : pcNodes) {
        if (selectedEntityId.equals(UtilClass.getCId(pcNode))) {
          return pciNode;
        }
      }
    }
    return null;
  }
  
  /**
   * @param mappingNode
   * @param mapForIdAndLabel
   * @return
   */
  private static List<String> getLinkedPropertyCollectionIds(Vertex mappingNode,
      Map<String, Object> mapForIdAndLabel, String relationLabel, String entityLabel)
  {
    List<String> pcIds = new ArrayList<>();
    Iterable<Vertex> pcNodes = UtilClass.getGraph()
        .command(new OCommandSQL("SELECT FROM (TRAVERSE OUT('"
            + relationLabel + "'), OUT('"
            + RelationshipLabelConstants.MAPPED_TO_ENTITY + "') FROM " + mappingNode.getId()
                .toString()
            + ") where @class = '" + entityLabel + "'"))
        .execute();
    for (Vertex pcNode : pcNodes) {
      String pcId = UtilClass.getCId(pcNode);
      Map<String, Object> entityDetails = new HashMap<>();
      entityDetails.put(IConfigEntityInformationModel.ID, pcId);
      entityDetails.put(IConfigEntityInformationModel.LABEL,
          (String) UtilClass.getValueByLanguage(pcNode, CommonConstants.LABEL_PROPERTY));
      entityDetails.put(IConfigEntityInformationModel.CODE,
          pcNode.getProperty(CommonConstants.CODE_PROPERTY));
      mapForIdAndLabel.put(pcId, entityDetails);
      pcIds.add(pcId);
    }
    return pcIds;
  }
  
  /**
   * @param profileNode
   * @param relationshipLabel
   * @param mapForIdAndLabel
   * @return
   */
  private static Map<String, Map<String, Object>> getEntityMapping(Vertex profileNode,
      String relationshipLabel, Map<String, Object> mapForIdAndLabel)
  {
    Iterable<Vertex> configRuleNodes = profileNode.getVertices(Direction.OUT, relationshipLabel);
    return getEntityMapping(mapForIdAndLabel, configRuleNodes, new ArrayList<>());
  }
  
  /**
   * @param mapForIdAndLabel
   * @param configRuleNodes
   * @return
   */
  private static Map<String, Map<String, Object>> getEntityMapping(
      Map<String, Object> mapForIdAndLabel, Iterable<Vertex> configRuleNodes,
      List<String> propertyIds)
  {
    Map<String, Map<String, Object>> configRuleEntityMappings = new HashMap<>();
    for (Vertex configRuleNode : configRuleNodes) {
      Boolean isMappingAlreadyExist = false;
      Map<String, Object> entityMapping = new HashMap<>();
      String columnName = "";
      entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
      List<String> columnNames = new ArrayList<>();
      entityMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, columnNames);
      Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMappingNode : columnMappingNodes) {
        columnName = (String) columnMappingNode
            .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
        columnNames.add(columnName);
      }
      
      Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.MAPPED_TO_ENTITY);
      for (Vertex entityMappingNode : entityMappingNodes) {
        String id = UtilClass.getCId(entityMappingNode);
        entityMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID, id);
        Map<String, Object> entityDetails = new HashMap<>();
        entityDetails.put(IConfigEntityInformationModel.ID, id);
        if (propertyIds.contains(id)) {
          isMappingAlreadyExist = true;
        }
        entityDetails.put(IConfigEntityInformationModel.LABEL, (String) UtilClass
            .getValueByLanguage(entityMappingNode, CommonConstants.LABEL_PROPERTY));
        entityDetails.put(IConfigEntityInformationModel.CODE,
            entityMappingNode.getProperty(CommonConstants.CODE_PROPERTY));
        mapForIdAndLabel.put(id, entityDetails);
      }
      if (!propertyIds.isEmpty() && !isMappingAlreadyExist) {
        continue;
      }
      configRuleEntityMappings.put(columnName, entityMapping);
    }
    return configRuleEntityMappings;
  }
  
  /**
   * @param profileNode
   * @param relationshipLabel
   * @param mapForIdAndLabel
   * @param intermediateNode
   * @param attrIds
   * @return
   */
  public static Map<String, Map<String, Object>> getAttributeMapping(Vertex profileNode,
      String relationshipLabel, Map<String, Object> mapForIdAndLabel, Vertex intermediateNode,
      List<String> attrIds)
  {
    Iterable<Vertex> configRuleNodes = intermediateNode.getVertices(Direction.OUT,
        relationshipLabel);
    return getEntityMapping(mapForIdAndLabel, configRuleNodes, attrIds);
  }
  
  /**
   * @param mappingNode
   * @param relationshipLabel
   * @param tagMapForIdAndLabel
   * @param intermediateNode
   * @return
   * @throws Exception
   */
  public static Map<String, Map<String, Object>> getTagMapping(Vertex mappingNode,
      String relationshipLabel, Map<String, Object> tagMapForIdAndLabel, Vertex intermediateNode,
      List<String> tagIds) throws Exception
  {
    Map<String, Map<String, Object>> configRuleTagMappings = new HashMap<>();
    Iterable<Vertex> configRuleNodes = intermediateNode.getVertices(Direction.OUT,
        relationshipLabel);
    for (Vertex configRuleNode : configRuleNodes) {
      List<Map<String, Object>> tagValuesMappingList = new ArrayList<>();
      Map<String, Object> tagMapping = new HashMap<>();
      String columnName = "";
      Boolean isMappingAlreadyExist = false;
      tagMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
      List<String> columnNames = new ArrayList<>();
      tagMapping.put(IConfigRuleTagMappingModel.COLUMN_NAMES, columnNames);
      Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMappingNode : columnMappingNodes) {
        Map<String, Object> tagValuesMapping = new HashMap<>();
        columnName = (String) columnMappingNode
            .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
        columnNames.add(columnName);
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
            String id = UtilClass.getCId(valueNode);
            tagValueMapping.put(ITagValueMappingModel.ID, id);
            tagValueMapping.put(ITagValueMappingModel.TAG_VALUE,
                (String) valueNode.getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME));
            tagValueMapping.put(ITagValueMappingModel.MAPPED_TAG_VALUE_ID,
                UtilClass.getCId(tagValueNode));
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
        String id = UtilClass.getCId(tagGroupMappingNode);
        tagMapping.put(IConfigRuleTagMappingModel.MAPPED_ELEMENT_ID, id);
        if (tagIds.contains(id)) {
          isMappingAlreadyExist = true;
        }
        Map<String, Object> entity = TagUtils.getTagMap(tagGroupMappingNode, false);
        tagMapForIdAndLabel.put(id, entity);
      }
      tagMapping.put(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS, tagValuesMappingList);
      
      if (!tagIds.isEmpty() && !isMappingAlreadyExist) {
        continue;
      }
      configRuleTagMappings.put(columnName, tagMapping);
    }
    
    return configRuleTagMappings;
  }
  
  public static void deleteEndpointMappings(Vertex mappingNode)
  {
    MappingUtils.deleteEntityMappings(mappingNode,
        RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE);
    MappingUtils.deleteEntityMappings(mappingNode,
        RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE);
    
    Iterable<Vertex> pciNodes = mappingNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    for (Vertex pciNode : pciNodes) {
      deletePropertyMappings(pciNode);
      pciNode.remove();
    }
  }
  
  /**
   * @param pciNode
   */
  private static void deletePropertyMappings(Vertex pciNode)
  {
    MappingUtils.deleteEntityMappings(pciNode,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
    Iterable<Vertex> tagConfigRules = pciNode.getVertices(Direction.OUT,
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
  
  /**
   * @param pciNode
   */
  private static void deletePCPropertyMappings(Vertex pciNode)
  {
    String pciId = UtilClass.getCId(pciNode);
    Iterable<Vertex> attributeConfigRules = pciNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
    for (Vertex attributeConfigRule : attributeConfigRules) {
      if (!isConfigRuleNodeShouldBeDeleted(attributeConfigRule,
          RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, pciId)) {
        continue;
      }
      Iterable<Vertex> columnMappings = attributeConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        columnMapping.remove();
      }
      attributeConfigRule.remove();
    }
    
    Iterable<Vertex> tagConfigRules = pciNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
    for (Vertex tagConfigRule : tagConfigRules) {
      if (!isConfigRuleNodeShouldBeDeleted(tagConfigRule,
          RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, pciId)) {
        continue;
      }
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
    pciNode.remove();
  }
  
  /**
   * @param mappingNode
   * @param mappingMap
   * @throws Exception
   */
  public static void saveMapping(Vertex mappingNode, Map<String, Object> mappingMap)
      throws Exception
  {
    List<String> addedPropertyCollectionIds = (List<String>) mappingMap
        .get("addedPropertyCollectionIds");
    List<String> deletedPropertyCollectionIds = (List<String>) mappingMap
        .get("deletedPropertyCollectionIds");
    Vertex pciNode = savePropertyCollectionMapping(mappingNode, addedPropertyCollectionIds,
        deletedPropertyCollectionIds, VertexLabelConstants.PROPERTY_COLLECTION, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    String selectedPropertyCollectionId = (String) mappingMap.get("selectedPropertyCollectionId");
    if (pciNode == null && selectedPropertyCollectionId != null) {
      pciNode = getPCINode(mappingNode, selectedPropertyCollectionId, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    }
    
    Vertex contextNode = null;
    String selectedContextId = (String) mappingMap.get("selectedContextId");
    if (selectedContextId != null) {
      contextNode = getPCINode(mappingNode, selectedContextId, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
    }
    
    addEdgeForExistingMappings(mappingMap, pciNode);
    
    List<Map<String, Object>> addedAttributesMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.ADDED_ATRRIBUTE_MAPPINGS);
    List<Map<String, Object>> modifiedAttributesMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.MODIFIED_ATTRIBUTE_MAPPINGS);
    saveAttributeMapping(pciNode, addedAttributesMapping, modifiedAttributesMapping);
   
    
    List<Map<String, Object>> addedTagsMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.ADDED_TAG_MAPPINGS);
    List<Map<String, Object>> modifiedTagsMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.MODIFIED_TAG_MAPPINGS);
    if (pciNode != null) {
      addTagMappings(pciNode, addedTagsMapping);
    }
    else if (contextNode != null) {
      addTagMappings(pciNode, addedTagsMapping);
    }
    modifyTagMappings(pciNode, modifiedTagsMapping);
    List<Map<String, Object>> addedClassesMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.ADDED_CLASS_MAPPINGS);
    List<Map<String, Object>> modifiedClassesMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.MODIFIED_CLASS_MAPPINGS);
    List<String> deletedClassesMapping = (List<String>) mappingMap
        .get(ISaveMappingModel.DELETED_CLASS_MAPPINGS);
    MappingUtils.saveClassMapping(mappingNode, addedClassesMapping, modifiedClassesMapping,
        deletedClassesMapping);
    
    List<Map<String, Object>> addedTaxonomiesMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.ADDED_TAXONOMY_MAPPINGS);
    List<Map<String, Object>> modifiedTaxonomiesMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.MODIFIED_TAXONOMY_MAPPINGS);
    List<String> deletedTaxonomiesMapping = (List<String>) mappingMap
        .get(ISaveMappingModel.DELETED_TAXONOMY_MAPPINGS);
    MappingUtils.saveTaxonomyMapping(mappingNode, addedTaxonomiesMapping, modifiedTaxonomiesMapping,
        deletedTaxonomiesMapping);
    
    List<Map<String, Object>> addedRelationshipsMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.ADDED_RELATIONSHIP_MAPPINGS);
    List<Map<String, Object>> modifiedRelationshipsMapping = (List<Map<String, Object>>) mappingMap
        .get(ISaveMappingModel.MODIFIED_RELATIONSHIP_MAPPINGS);
    List<String> deletedRelationshipsMapping = (List<String>) mappingMap
        .get(ISaveMappingModel.DELETED_RELATIONSHIP_MAPPINGS);
    MappingUtils.saveRelationshipMapping(mappingNode, addedRelationshipsMapping, modifiedRelationshipsMapping,
        deletedRelationshipsMapping);
  }
  
  /**
   * @param mappingNode
   * @param addedPropertyCollectionIds
   * @param deletedPropertyCollectionIds
   * @return
   * @throws Exception
   */
  public static Vertex savePropertyCollectionMapping(Vertex mappingNode,
      List<String> addedPropertyCollectionIds, List<String> deletedPropertyCollectionIds, String entityLabel, String configRuleEdgeLable)
      throws Exception
  {
    for (String pcId : addedPropertyCollectionIds) {
      Vertex pcNode = UtilClass.getVertexById(pcId, entityLabel);
      OrientVertexType vertexType = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CID_PROPERTY);
      Map<String, Object> pcMap = new HashMap<>();
      pcMap.put(CommonConstants.CID_PROPERTY, UUID.randomUUID()
          .toString());
      Vertex configRuleNode = UtilClass.createNode(pcMap, vertexType, new ArrayList<>());
      mappingNode.addEdge(configRuleEdgeLable,
          configRuleNode);
      configRuleNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, pcNode);
      return configRuleNode;
    }
    
    for (String pcId : deletedPropertyCollectionIds) {
      Vertex pciNode = getPCINode(mappingNode, pcId, configRuleEdgeLable);
      deletePCPropertyMappings(pciNode);
    }
    
    return null;
  }
  
  /**
   * @param pciNode
   * @param addedAttributesMapping
   * @param modifiedAttributesMapping
   * @param modifiedAttributesMapping2
   * @throws Exception
   */
  public static void saveAttributeMapping(Vertex pciNode,
      List<Map<String, Object>> addedAttributesMapping,
      List<Map<String, Object>> modifiedAttributesMapping) throws Exception
  {
    addEntityMappings(pciNode, addedAttributesMapping, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
    MappingUtils.modifyEntityMappings(pciNode, modifiedAttributesMapping);
  }
  
  /**
   * @param profileNode
   * @param addedEntitiesMapping
   * @param entityType
   * @param relationshipLabel
   * @throws Exception
   */
  private static void addEntityMappings(Vertex profileNode,
      List<Map<String, Object>> addedEntitiesMapping, String entityType, String relationshipLabel)
      throws Exception
  {
    for (Map<String, Object> addedEntityMapping : addedEntitiesMapping) {
      OrientVertexType vertexType = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CID_PROPERTY);
      Vertex configRuleNode = UtilClass.createNode(addedEntityMapping, vertexType,
          Arrays.asList(IConfigRuleAttributeMappingModel.COLUMN_NAMES,
              IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID));
      profileNode.addEdge(relationshipLabel, configRuleNode);
      for (String columnName : (List<String>) addedEntityMapping
          .get(IConfigRuleAttributeMappingModel.COLUMN_NAMES)) {
        vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
            CommonConstants.CID_PROPERTY);
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
  
  /**
   * @param pciNode
   * @param addedTagsMapping
   * @param modifiedTagsMapping
   * @param modifiedTagsMapping2
   * @throws Exception
   */
  public static void saveTagMapping(Vertex pciNode, List<Map<String, Object>> addedTagsMapping,
      List<Map<String, Object>> modifiedTagsMapping) throws Exception
  {
    addTagMappings(pciNode, addedTagsMapping);
    modifyTagMappings(pciNode, modifiedTagsMapping);
  }
  
  /**
   * @param pciNode
   * @param addedTagsMapping
   * @throws Exception
   */
  private static void addTagMappings(Vertex pciNode, List<Map<String, Object>> addedTagsMapping)
      throws Exception
  {
    for (Map<String, Object> addedTagMapping : addedTagsMapping) {
      OrientVertexType vertexType = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CID_PROPERTY);
      Vertex configRuleNode = UtilClass.createNode(addedTagMapping, vertexType,
          Arrays.asList(IConfigRuleTagMappingModel.COLUMN_NAMES,
              IConfigRuleTagMappingModel.MAPPED_ELEMENT_ID,
              IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS));
      pciNode.addEdge(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, configRuleNode);
      for (String columnName : (List<String>) addedTagMapping
          .get(IConfigRuleAttributeMappingModel.COLUMN_NAMES)) {
        vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
            CommonConstants.CID_PROPERTY);
        Map<String, Object> columnMappingMap = new HashMap<>();
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
                CommonConstants.CID_PROPERTY);
            if (((String) tagValueMapping.get(IColumnValueTagValueMappingModel.COLUMN_NAME))
                .equals(columnName)) {
              List<Map<String, Object>> tvMappingList = (List<Map<String, Object>>) tagValueMapping
                  .get(IColumnValueTagValueMappingModel.MAPPINGS);
              for (Map<String, Object> tvMapping : tvMappingList) {
                Map<String, Object> tagValueMap = new HashMap<>();
                tagValueMap.put(IConfigEntityInformationModel.ID,
                    tvMapping.get(ITagValueMappingModel.ID));
                tagValueMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME,
                    tvMapping.get(ITagValueMappingModel.TAG_VALUE));
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
  
  /**
   * @param pciNode
   * @param modifiedTagsMapping
   * @throws Exception
   */
  private static void modifyTagMappings(Vertex pciNode,
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
        columnMappingNode.setProperty(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
        List<Map<String, Object>> tagValueMappingList = (List<Map<String, Object>>) modifiedTagMapping
            .get(IConfigRuleTagOutBoundMappingModel.TAG_VALUE_MAPPINGS);
        
        if (tagValueMappingList != null && tagValueMappingList.size() > 0) {
          for (Map<String, Object> tagValueMapping : tagValueMappingList) {
            List<Map<String, Object>> valueMappings = (List<Map<String, Object>>) tagValueMapping
                .get(IColumnValueTagValueMappingModel.MAPPINGS);
            for (Map<String, Object> valueMapping : valueMappings) {
              Vertex valueNode = UtilClass.getVertexById(
                  (String) valueMapping.get(ITagValueMappingModel.ID), VertexLabelConstants.VALUE);
              valueNode.setProperty(IColumnValueTagValueMappingModel.COLUMN_NAME,
                  valueMapping.get(ITagValueMappingModel.TAG_VALUE));
            }
          }
        }
      }
    }
  }
  
  /**
   * @param configRule
   * @param relationshipLabel
   * @param pciId
   */
  private static Boolean isConfigRuleNodeShouldBeDeleted(Vertex configRule,
      String relationshipLabel, String pciId)
  {
    Iterable<Edge> configRuleEdges = configRule.getEdges(Direction.IN, relationshipLabel);
    Integer edgeCount = 0;
    for (Edge configRuleEdge : configRuleEdges) {
      edgeCount++;
      Vertex pciNode = configRuleEdge.getVertex(Direction.OUT);
      if (pciId.equals(UtilClass.getCId(pciNode))) {
        configRuleEdge.remove();
      }
    }
    if (edgeCount == 1) {
      return true;
    }
    return false;
  }
  
  /**
   * @param mappingNode
   * @param returnMap
   * @param mappingHelperModel
   * @throws Exception
   */
  public static void getMapFromMappingNodeForExport(Vertex mappingNode,
      Map<String, Object> returnMap, IMappingHelperModel mappingHelperModel) throws Exception
  {
    returnMap.put(IMappingModel.CLASS_MAPPINGS,
        new ArrayList<>(new TreeMap<String, Object>(getEntityMapping(mappingNode,
            RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE, mappingHelperModel.getConfigDetails()
                .getKlasses())).values()));
    returnMap.put(IMappingModel.TAXONOMY_MAPPINGS,
        new ArrayList<>(new TreeMap<String, Object>(
            getEntityMapping(mappingNode, RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE,
                mappingHelperModel.getConfigDetails()
                    .getTaxonomy())).values()));
    getPropertiesMapping(mappingNode, mappingHelperModel, returnMap);
    returnMap
        .put(IMappingModel.RELATIONSHIP_MAPPINGS,
            new ArrayList<>(new TreeMap<String, Object>(getEntityMapping(mappingNode,
                RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE,
                mappingHelperModel.getConfigDetails()
                    .getRelationships())).values()));
    getContextTagMapping(mappingNode, mappingHelperModel, returnMap);
  }
  
  /**
   * @param mappingMap
   * @param pciNode
   * @throws Exception
   */
  private static void addEdgeForExistingMappings(Map<String, Object> mappingMap, Vertex pciNode)
      throws Exception
  {
    List<String> attrCRIds = (List<String>) mappingMap
        .get(ISaveOutBoundMappingModel.CONFIG_RULE_IDS_FOR_ATTRIBUTE);
    for (String crId : attrCRIds) {
      Vertex crNode = UtilClass.getVertexById(crId, VertexLabelConstants.CONFIG_RULE);
      pciNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, crNode);
    }
    
    List<String> tagCRIds = (List<String>) mappingMap
        .get(ISaveOutBoundMappingModel.CONFIG_RULE_IDS_FOR_TAG);
    for (String crId : tagCRIds) {
      Vertex crNode = UtilClass.getVertexById(crId, VertexLabelConstants.CONFIG_RULE);
      pciNode.addEdge(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, crNode);
    }
  }
  
  /**
   * @param propertyCollection
   * @param propertyCollectionNode
   * @throws Exception
   */
  public static void manageAddedDeletedElementsForMappings(Map<String, Object> propertyCollection,
      Vertex propertyCollectionNode) throws Exception
  {
    List<Map<String, Object>> addedElements = (List<Map<String, Object>>) propertyCollection
        .get(ISavePropertyCollectionModel.ADDED_ELEMENTS);
    Map<String, Vertex> addedAttributeIdsMap = new HashMap<>();
    Map<String, Vertex> addedTagIdsMap = new HashMap<>();
    
    Iterable<Vertex> pciNodes = UtilClass.getGraph()
        .command(new OCommandSQL(
            "SELECT FROM (TRAVERSE in('" + RelationshipLabelConstants.MAPPED_TO_ENTITY + "')"
                + " FROM " + propertyCollectionNode.getId()
                    .toString()
                + ") where @class = '" + VertexLabelConstants.CONFIG_RULE + "'"))
        .execute();
    
    for (Map<String, Object> addedElement : addedElements) {
      String elementId = (String) addedElement.get(IPropertyCollectionElement.ID);
      String elementType = (String) addedElement.get(IPropertyCollectionElement.TYPE);
      Vertex elementNode = PropertyCollectionUtils.getElementVertexFromIdAndType(elementId,
          elementType);
      
      if (elementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
        addedAttributeIdsMap.put(elementId, elementNode);
      }
      else if (elementType.equals(CommonConstants.TAG_PROPERTY)) {
        addedTagIdsMap.put(elementId, elementNode);
      }
    }
    
    if (!addedAttributeIdsMap.isEmpty() || !addedTagIdsMap.isEmpty()) {
      for (Vertex pciNode : pciNodes) {
        Iterable<Vertex> mappingNodes = pciNode.getVertices(Direction.IN,
            RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
        for (Vertex mappingNode : mappingNodes) {
          for (String attrId : addedAttributeIdsMap.keySet()) {
            linkAddedElements(
                getPropertyMappingDataToAdd(addedAttributeIdsMap.get(attrId),
                    CommonConstants.ATTRIBUTE_PROPERTY),
                pciNode, mappingNode, attrId, true,
                RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
          }
          for (String tagId : addedTagIdsMap.keySet()) {
            linkAddedElements(
                getPropertyMappingDataToAdd(addedTagIdsMap.get(tagId),
                    CommonConstants.TAG_PROPERTY),
                pciNode, mappingNode, tagId, false, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
          }
        }
      }
    }
    
    List<Map<String, Object>> deletedElements = (List<Map<String, Object>>) propertyCollection
        .get(ISavePropertyCollectionModel.DELETED_ELEMENTS);
    for (Map<String, Object> deletedElement : deletedElements) {
      String elementId = (String) deletedElement.get(IPropertyCollectionElement.ID);
      String elementType = (String) deletedElement.get(IPropertyCollectionElement.TYPE);
      for (Vertex pciNode : pciNodes) {
        if (elementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
          OutboundMappingUtils.deletePCPropertyMapping(pciNode, elementId, elementType);
        }
        else if (elementType.equals(CommonConstants.TAG_PROPERTY)) {
          OutboundMappingUtils.deletePCPropertyMapping(pciNode, elementId, elementType);
        }
      }
    }
  }
  
  /**
   * @param addedPropertyIdsList
   * @param pciNode
   * @param mappingNode
   * @param propertyId
   * @param isAttribute
   * @param relationshipLabel
   * @throws Exception
   */
  private static void linkAddedElements(List<Map<String, Object>> addedPropertyIdsList,
      Vertex pciNode, Vertex mappingNode, String propertyId, Boolean isAttribute,
      String relationshipLabel) throws Exception
  {
    Iterable<Vertex> result = UtilClass.getGraph()
        .command(new OCommandSQL("SELECT $path FROM (TRAVERSE out('"
            + RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE + "'),out('"
            + relationshipLabel + "'),out('" + RelationshipLabelConstants.MAPPED_TO_ENTITY + "')"
            + " FROM " + mappingNode.getId()
                .toString()
            + ") where cid = '" + propertyId + "'"))
        .execute();
    if (!result.iterator()
        .hasNext()) {
      if (isAttribute) {
        OutboundMappingUtils.addEntityMappings(pciNode, addedPropertyIdsList,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, relationshipLabel);
      }
      else {
        OutboundMappingUtils.addTagMappings(pciNode, addedPropertyIdsList);
      }
    }
    else {
      String path = result.iterator()
          .next()
          .getProperty("$path");
      String[] split = path.split("\\.");
      Vertex configRuleNode = UtilClass.getGraph()
          .getVertex(StringUtils.substringBetween(split[2], "(", ")"));
      pciNode.addEdge(relationshipLabel, configRuleNode);
    }
  }
  
  /**
   * @param elementNode
   * @param elementType
   * @return
   */
  private static List<Map<String, Object>> getPropertyMappingDataToAdd(Vertex elementNode,
      String elementType)
  {
    List<Map<String, Object>> propertyMappings = new ArrayList<>();
    Map<String, Object> propertyMapping = new HashMap<>();
    propertyMapping.put(IConfigRuleAttributeOutBoundMappingModel.ID, UUID.randomUUID()
        .toString());
    propertyMapping.put(IConfigRuleAttributeOutBoundMappingModel.IS_IGNORED, false);
    propertyMapping.put(IConfigRuleAttributeOutBoundMappingModel.MAPPED_ELEMENT_ID,
        UtilClass.getCId(elementNode));
    String elementCode = elementNode.getProperty(IConfigEntity.CODE);
    propertyMapping.put(IConfigRuleAttributeOutBoundMappingModel.COLUMN_NAMES,
        Arrays.asList(elementCode));
    if (elementType.equals(CommonConstants.TAG_PROPERTY)) {
      List<Map<String, Object>> mappings = new ArrayList<>();
      Map<String, Object> tagValueMappings = new HashMap<>();
      Iterable<Vertex> vertices = elementNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      for (Vertex childTagVertex : vertices) {
        Map<String, Object> tagValueMapping = new HashMap<>();
        tagValueMapping.put(ITagValueMappingModel.ID, UUID.randomUUID()
            .toString());
        tagValueMapping.put(ITagValueMappingModel.MAPPED_TAG_VALUE_ID,
            UtilClass.getCId(childTagVertex));
        tagValueMapping.put(ITagValueMappingModel.TAG_VALUE,
            (String) childTagVertex.getProperty(IConfigEntity.CODE));
        mappings.add(tagValueMapping);
      }
      tagValueMappings.put(IColumnValueTagValueMappingModel.MAPPINGS, mappings);
      tagValueMappings.put(IColumnValueTagValueMappingModel.COLUMN_NAME, elementCode);
      propertyMapping.put(IConfigRuleTagOutBoundMappingModel.TAG_VALUE_MAPPINGS,
          Arrays.asList(tagValueMappings));
    }
    propertyMappings.add(propertyMapping);
    return propertyMappings;
  }
  
  /**
   * @param pciNode
   * @param elementId
   * @param elementType
   */
  private static void deletePCPropertyMapping(Vertex pciNode, String elementId, String elementType)
  {
    String pciId = UtilClass.getCId(pciNode);
    if (elementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
      Iterable<Vertex> attributeConfigRules = pciNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
      for (Vertex attributeConfigRule : attributeConfigRules) {
        Iterable<Vertex> mappedEntities = attributeConfigRule.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex mappedEntity : mappedEntities) {
          if (!elementId.equals(UtilClass.getCId(mappedEntity))) {
            continue;
          }
          if (!isConfigRuleNodeShouldBeDeleted(attributeConfigRule,
              RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, pciId)) {
            continue;
          }
          Iterable<Vertex> columnMappings = attributeConfigRule.getVertices(Direction.OUT,
              RelationshipLabelConstants.HAS_COLUMN_MAPPING);
          for (Vertex columnMapping : columnMappings) {
            columnMapping.remove();
          }
          attributeConfigRule.remove();
        }
      }
    }
    else {
      Iterable<Vertex> tagConfigRules = pciNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
      for (Vertex tagConfigRule : tagConfigRules) {
        Iterable<Vertex> mappedEntities = tagConfigRule.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex mappedEntity : mappedEntities) {
          if (!elementId.equals(UtilClass.getCId(mappedEntity))) {
            continue;
          }
          if (!isConfigRuleNodeShouldBeDeleted(tagConfigRule,
              RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, pciId)) {
            continue;
          }
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
    }
  }
  
  /**
   * @param tagNode
   * @param parentTagNode
   * @throws Exception
   */
  public static void manageAddedTagValueForMappings(Vertex tagNode, Vertex parentTagNode)
      throws Exception
  {
    if (parentTagNode == null) {
      return;
    }
    Iterable<Vertex> paths = UtilClass.getGraph()
        .command(new OCommandSQL("SELECT $path FROM (TRAVERSE in('"
            + RelationshipLabelConstants.MAPPED_TO_ENTITY + "')," + "in('"
            + RelationshipLabelConstants.HAS_TAG_CONFIG_RULE + "')," + "in('"
            + RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE + "')" + " FROM "
            + parentTagNode.getId()
                .toString()
            + ") where @class = '" + VertexLabelConstants.PROPERTY_MAPPING + "'"))
        .execute();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.VALUE,
        CommonConstants.CID_PROPERTY);
    for (Vertex pathNode : paths) {
      String path = pathNode.getProperty("$path");
      String[] split = path.split("\\.");
      Vertex configRuleNode = UtilClass.getGraph()
          .getVertex(StringUtils.substringBetween(split[1], "(", ")"));
      Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMappingNode : columnMappingNodes) {
        Map<String, Object> tagValueMap = new HashMap<>();
        tagValueMap.put(IConfigEntityInformationModel.ID, UUID.randomUUID()
            .toString());
        tagValueMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME,
            tagNode.getProperty(IConfigEntity.CODE));
        Vertex tagValueNode = UtilClass.createNode(tagValueMap, vertexType, new ArrayList<>());
        columnMappingNode.addEdge(RelationshipLabelConstants.HAS_VALUE_MAPPING, tagValueNode);
        tagValueNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, tagNode);
      }
    }
  }
  
  public static void addAndRemoveMappingEntity(Vertex mappingNode, Map<String, Object> mappingMap) throws Exception
  {
    Set<String> deletedRelationshipsIds = new HashSet<>();
    Set<String> deletedRelationshipsINodes = new HashSet<>();
    Set<String> deletedPropertyCollectionIds = new HashSet<>();
    Set<String> deletedContextIds = new HashSet<>();

    Set<String> existingPropertyCollectionIds = new HashSet<>();
    Set<String> existingRelationshipsIds = new HashSet<>();
    Set<String> existingAttributeMappingIds = new HashSet<>();
    Set<String> existingTagMappingIds = new HashSet<>();
    Set<String> existingContextIds = new HashSet<>();
    Set<String> existingContextTagIds = new HashSet<>();
    
    
    List<String> deletedClassesAndTaxonomyMapping = new ArrayList<>();
    deletedClassesAndTaxonomyMapping.addAll((List<String>) mappingMap.get(ISaveMappingModel.DELETED_CLASS_MAPPINGS));
    deletedClassesAndTaxonomyMapping.addAll((List<String>) mappingMap.get(ISaveMappingModel.DELETED_TAXONOMY_MAPPINGS));
    
    Iterable<Vertex> klassIVertices = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE);
    Iterable<Vertex> taxonomyIVertices = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE);
    Set<String> klassAndTaxonomyINodes = new HashSet<>(UtilClass.getCodes(klassIVertices));
    klassAndTaxonomyINodes.addAll(UtilClass.getCodes(taxonomyIVertices));
    klassAndTaxonomyINodes.removeAll(deletedClassesAndTaxonomyMapping);
    
  //Get PCs, Context and Relationship Ids from existing classes
    for (String klassINode : klassAndTaxonomyINodes) {
      getRelationshipIdsAndPCIdsFromINodeId(existingRelationshipsIds, existingPropertyCollectionIds, klassINode, existingAttributeMappingIds, existingTagMappingIds, existingContextIds, existingContextTagIds);
    }
    
    //Get PCs, Context and Relationship Ids from Klass to be deleted
    for (String deletedClassMapping : deletedClassesAndTaxonomyMapping) {
      getRelationshipIdsAndPCIdsFromINodeId(deletedRelationshipsIds, deletedPropertyCollectionIds, deletedClassMapping, new HashSet<>(), new HashSet<>(), deletedContextIds, new HashSet<>());
    }
    
    //Manage deleted Relationship
    List<String> finalRelationshipIDsToBeDeleted = new ArrayList<>(deletedRelationshipsIds);
    finalRelationshipIDsToBeDeleted.removeAll(existingRelationshipsIds);
    
    for (String relId : finalRelationshipIDsToBeDeleted) {
      Iterable<Vertex> relINodes = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE);
      for (Vertex relINode : relINodes) {
        Iterable<Vertex> pcNodes = relINode.getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex pcNode : pcNodes) {
          if (relId.equals(UtilClass.getCId(pcNode))) {
            deletedRelationshipsINodes.add(UtilClass.getCodeNew(relINode));
          }
        }
      }
    }
    MappingUtils.saveRelationshipMapping(mappingNode, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(deletedRelationshipsINodes));
    
    // Manage deleted PCs   
    List<String> finalPCIDsToBeDeleted = new ArrayList<>(deletedPropertyCollectionIds);
    finalPCIDsToBeDeleted.removeAll(existingPropertyCollectionIds);
    savePropertyCollectionMapping(mappingNode, new ArrayList<>(), new ArrayList<>(finalPCIDsToBeDeleted),
        VertexLabelConstants.PROPERTY_COLLECTION, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    
    //Manage deleted context
    deletedContextIds.removeAll(existingContextIds);
    savePropertyCollectionMapping(mappingNode, new ArrayList<>(), new ArrayList<>(deletedContextIds),
        VertexLabelConstants.VARIANT_CONTEXT, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
    
    /*Manage added classses and taxonomies*/
    List<Vertex> classAndTaxonomyVertices = new ArrayList<>();
    List<Map<String, Object>> addedClassesMapping = (List<Map<String, Object>>) mappingMap.get(ISaveMappingModel.ADDED_CLASS_MAPPINGS);
    for (Map<String, Object> addedClassMapping : addedClassesMapping) {
      classAndTaxonomyVertices.add(getNodeById(addedClassMapping, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS));      
    }
    
    List<Map<String, Object>> addedTaxonomiesMapping= (List<Map<String, Object>>) mappingMap.get(ISaveMappingModel.ADDED_TAXONOMY_MAPPINGS);
    for (Map<String, Object> addedTaxonomyMapping : addedTaxonomiesMapping) {
      classAndTaxonomyVertices.add(getNodeById(addedTaxonomyMapping, VertexLabelConstants.ROOT_KLASS_TAXONOMY));      
    }
    
    for (Vertex klassAndTaxonomyNode : classAndTaxonomyVertices) {
      List<Map<String, Object>> addedRelationshipsMapping = new ArrayList<>();
      
      //Manage added or modifies PCs
      Iterable<Vertex> sectionVertices = klassAndTaxonomyNode.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      for (Vertex sectionVertex : sectionVertices) {
        Vertex propertyCollectionVertex = KlassUtils.getPropertyCollectionNodeFromKlassSectionNode(sectionVertex);
        String propertyCode = UtilClass.getCodeNew(propertyCollectionVertex);
        
        if (!existingPropertyCollectionIds.contains(propertyCode)) {
          existingPropertyCollectionIds.add(propertyCode);
          linkPropertyCollection(mappingNode, existingAttributeMappingIds, existingTagMappingIds,
              propertyCollectionVertex, propertyCode);
        }
      }
      
      //Manage added or modifies Relationships
      String query = "SELECT FROM (SELECT EXPAND (OUT('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) FROM " + klassAndTaxonomyNode.getId()
          + " ) WHERE " + ISectionElement.TYPE + " = " + EntityUtil.quoteIt(SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP);
      Iterable<Vertex> klassRelationshipNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      Set<Vertex> relationshipNodes = new HashSet<>();
      for (Vertex klassRelationship : klassRelationshipNodes) {
        //prepare relationshipNodes list for getting relationship variant context.
        relationshipNodes.add(klassRelationship);
        if (!(Boolean) klassRelationship.getProperty(ISectionRelationship.IS_NATURE)) {
          Iterator<Vertex> relationshipIterator = klassRelationship.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
              .iterator();
          Vertex relationshipNode = relationshipIterator.next();
          String relationshipCode = UtilClass.getCodeNew(relationshipNode);
          if (!existingRelationshipsIds.contains(relationshipCode)) {
            existingRelationshipsIds.add(relationshipCode);
            prepareADMForEntity(Arrays.asList(UtilClass.getMapFromVertex(FIELDS_TO_FETCH, relationshipNode)), addedRelationshipsMapping,
                Arrays.asList(relationshipCode));
          }
        }
      }
      MappingUtils.saveRelationshipMapping(mappingNode, addedRelationshipsMapping, new ArrayList<>(), new ArrayList<>());
      
      //Manage added Context
      Iterable<Vertex> contextVertices = getAllContextVertices(klassAndTaxonomyNode, relationshipNodes);
      for (Vertex contextVertex : contextVertices) {
        String contextCode = UtilClass.getCodeNew(contextVertex);
        
        if (!existingContextIds.contains(contextCode)) {
          existingContextIds.add(contextCode);
          linkContext(mappingNode, existingTagMappingIds, existingContextTagIds,
              contextVertex, contextCode);
        }
      }
    }
  }

  private static void linkContext(Vertex mappingNode, Set<String> existingPCTagIds, Set<String> existingContextTagIds,
      Vertex contextVertex, String contextCode) throws Exception
  {
    List<String> contextTagIds = new ArrayList<>();
    Map<String, Object> conetxtTagMap = new HashMap<>();
    List<Map<String, Object>> addedTagsMapping = new ArrayList<>();
    
    GetPropertyGroupInfo.getContextTags(contextVertex, conetxtTagMap, contextTagIds);
    Vertex contextCRNode = savePropertyCollectionMapping(mappingNode, Arrays.asList(contextCode), new ArrayList<>(),
        VertexLabelConstants.VARIANT_CONTEXT, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
    
    for (String contextTagId : contextTagIds) {
      if (existingPCTagIds.contains(contextTagId)) {
        linkPCIToEntityConfigRule(mappingNode, contextCRNode, contextTagId, VertexLabelConstants.ENTITY_TAG,
            RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
      }else if(existingContextTagIds.contains(contextTagId)){
        linkPCIToEntityConfigRule(mappingNode, contextCRNode, contextTagId, VertexLabelConstants.ENTITY_TAG,
            RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
      }
    }
    contextTagIds.removeAll(existingPCTagIds);
    contextTagIds.removeAll(existingContextTagIds);
    
    prepareADMForEntity((List<Map<String, Object>>) conetxtTagMap.get(IGetPropertyGroupInfoResponseModel.TAG_LIST), addedTagsMapping,
        contextTagIds);
    saveTagMapping(contextCRNode, addedTagsMapping, new ArrayList<>());
    existingContextTagIds.addAll(contextTagIds);
  }

  private static void linkPropertyCollection(Vertex mappingNode, Set<String> existingAttributeMappingIds, Set<String> existingTagMappingIds,
      Vertex propertyCollectionVertex, String propertyCode) throws Exception
  {
    List<String> pcAttrIds = new ArrayList<>();
    List<String> pcTagIds = new ArrayList<>();
    Map<String, Object> pcAttrTagMap = new HashMap<>();
    List<Map<String, Object>> addedAttributesMapping = new ArrayList<>();
    List<Map<String, Object>> addedTagsMapping = new ArrayList<>();
    
    GetPropertyGroupInfo.getPropertyCollection(propertyCollectionVertex, pcAttrTagMap, pcAttrIds, pcTagIds);
    Vertex pciNode = savePropertyCollectionMapping(mappingNode, Arrays.asList(propertyCode), new ArrayList<>(),
        VertexLabelConstants.PROPERTY_COLLECTION, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    
    for (String pcAttrId : pcAttrIds) {
      if (existingAttributeMappingIds.contains(pcAttrId)) {
        linkPCIToEntityConfigRule(mappingNode, pciNode, pcAttrId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
            RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
      }
    }
    pcAttrIds.removeAll(existingAttributeMappingIds);
    
    for (String pcTagId : pcTagIds) {
      if (existingTagMappingIds.contains(pcTagId)) {
        linkPCIToEntityConfigRule(mappingNode, pciNode, pcTagId, VertexLabelConstants.ENTITY_TAG,
            RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
      }
    }
    pcTagIds.removeAll(existingTagMappingIds);

    prepareADMForEntity((List<Map<String, Object>>) pcAttrTagMap.get(IGetPropertyGroupInfoResponseModel.ATTRIBUTE_LIST),
        addedAttributesMapping, pcAttrIds);
    saveAttributeMapping(pciNode, addedAttributesMapping, new ArrayList<>());
    existingAttributeMappingIds = new HashSet<>(existingAttributeMappingIds);
    existingAttributeMappingIds.addAll(pcAttrIds);
    
    prepareADMForEntity((List<Map<String, Object>>) pcAttrTagMap.get(IGetPropertyGroupInfoResponseModel.TAG_LIST), addedTagsMapping,
        pcTagIds);
    saveTagMapping(pciNode, addedTagsMapping, new ArrayList<>());
    existingTagMappingIds.addAll(new HashSet<>(pcTagIds));
  }

  private static Vertex getNodeById(Map<String, Object> classMapping, String relationshipEdge) throws Exception
  {
    Vertex klassNode = UtilClass.getVertexById((String) classMapping.get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID),
        relationshipEdge);
    return klassNode;
  }

  private static void linkPCIToEntityConfigRule(Vertex mappingNode, Vertex pciNode, String entityId, String entityType,
      String relationshipEdge, String entityRelatiopnShipLabel) throws Exception
  {
    //TODO: chnage pcNodes => propertyCRNodes
    Iterable<Vertex> pcNodes = UtilClass.getGraph()
        .command(new OCommandSQL("SELECT FROM (TRAVERSE OUT('"
            + entityRelatiopnShipLabel + "'), OUT('"
            + relationshipEdge +  "'), OUT('"
            + RelationshipLabelConstants.MAPPED_TO_ENTITY + "') FROM " + mappingNode.getId()
                .toString()
            + ") where @class = '" + VertexLabelConstants.CONFIG_RULE + "'"))
        .execute();
    for (Vertex CRNode : pcNodes) {
      Iterable<Vertex> entityNode = CRNode.getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY);
      if (entityNode.iterator().hasNext()) {
        Vertex next = entityNode.iterator().next();
        if (UtilClass.getCodeNew(next).equals(entityId)) {
          pciNode.addEdge(relationshipEdge, CRNode);
          break;
        }
      }
    }
  }

  private static void getRelationshipIdsAndPCIdsFromINodeId(Set<String> relationshipsIds, Set<String> propertyCollectionIds,
      String klassINode, Set<String> attrIds, Set<String> tagIds, Set<String> existingContextIds, Set<String> existingContextTagIds) throws Exception
  {
    Vertex configNode = UtilClass.getVertexById(klassINode, VertexLabelConstants.CONFIG_RULE);
    Iterable<Vertex> klassNode = configNode.getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY);
    List<String> existingAttrIds = new ArrayList<>();
    List<String> existingTagIds = new ArrayList<>();
    
    if (klassNode.iterator().hasNext()) {
      Vertex next = klassNode.iterator().next();
      Iterable<Vertex> sectionVertices = next.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      for (Vertex sectionVertex : sectionVertices) {
        Vertex propertyCollectionVertex = KlassUtils.getPropertyCollectionNodeFromKlassSectionNode(sectionVertex);
        propertyCollectionIds.add(UtilClass.getCodeNew(propertyCollectionVertex));
        GetPropertyGroupInfo.getPropertyCollection(propertyCollectionVertex, new HashMap<>(), existingAttrIds, existingTagIds);
      }
      
      attrIds.addAll(existingAttrIds);
      tagIds.addAll(existingTagIds);
      
      String query = "SELECT FROM (SELECT EXPAND (OUT('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) FROM " + next.getId()
          + " ) WHERE " + ISectionElement.TYPE + " = " + EntityUtil.quoteIt(SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP);
      Iterable<Vertex> klassRelationshipNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      Set<Vertex> relationshipNodes = new HashSet<>();
      for (Vertex klassRelationship : klassRelationshipNodes) {
        //prepare relationshipNodes list for getting relationship variant context.
        relationshipNodes.add(klassRelationship);
        if (!(Boolean) klassRelationship.getProperty(ISectionRelationship.IS_NATURE)) {
          Iterator<Vertex> relationshipIterator = klassRelationship.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
              .iterator();
          Vertex relationshipNode = relationshipIterator.next();
          String relationshipCode = UtilClass.getCodeNew(relationshipNode);
          relationshipsIds.add(relationshipCode);
        }
      }
      
      //Klass to variant context
      getLinkedContextToClass(existingContextIds, existingContextTagIds, relationshipNodes, next);
    }
  }

  private static void getLinkedContextToClass(Set<String> existingContextIds, Set<String> existingContextTagIds, Set<Vertex> klassRelationshipNodes, Vertex next)
  {
    Set<Vertex> allContextVertices = getAllContextVertices(next, klassRelationshipNodes);
    for (Vertex contextVertex : allContextVertices) {
      existingContextIds.add(UtilClass.getCode(contextVertex));
      
      Iterator<Vertex> contextTags = contextVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG).iterator();
      while (contextTags.hasNext()) {
        Iterator<Vertex> contextTagPropertyNodes = contextTags.next()
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY).iterator();
        while (contextTagPropertyNodes.hasNext()) {
          existingContextTagIds.add(UtilClass.getCId(contextTagPropertyNodes.next()));
        }
      }
    }
  }

  private static Set<Vertex> getAllContextVertices(Vertex klassAndTaxonomyNode, Set<Vertex> klassRelationshipNodes)
  {
    Set<Vertex> contextVertex = new HashSet<>();
    Iterator<Vertex> contextVertices = klassAndTaxonomyNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS).iterator();
    while (contextVertices.hasNext()) {
      Vertex contextKlassNode = contextVertices.next();
      Iterator<Vertex> contextVariantNode = contextKlassNode.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      while (contextVariantNode.hasNext()) {
        contextVertex.add(contextVariantNode.next());
      }
    }
    
    //Direct to variant context
    Iterator<Vertex> contextVariantNode = klassAndTaxonomyNode.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
    while (contextVariantNode.hasNext()) {
      contextVertex.add(contextVariantNode.next());
    }
    
    // nature relationship context
    Iterator<Vertex> natureRelationships = klassAndTaxonomyNode.getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
        .iterator();
    while (natureRelationships.hasNext()) {
      Vertex natureRelationshipNode = natureRelationships.next();
      Iterator<Vertex> contextVariantNodeNR = natureRelationshipNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
      while (contextVariantNodeNR.hasNext()) {
        contextVertex.add(contextVariantNodeNR.next());
      }
    }
    
    // Relationship variant context
    for (Vertex klassRelationshipNode : klassRelationshipNodes) {
      Iterator<Vertex> relationshipVariantContext = klassRelationshipNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF).iterator();
      while (relationshipVariantContext.hasNext()) {
        contextVertex.add(relationshipVariantContext.next());
      }
    }
    
     String query = "SELECT FROM (SELECT EXPAND (OUT('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) FROM " + klassAndTaxonomyNode.getId()
     + " ) WHERE " + ISectionElement.TYPE + " = " + EntityUtil.quoteIt(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE);
    Iterable<Vertex> klassProperyNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for(Vertex klassProperyNode : klassProperyNodes) {
      Iterator<Vertex> attributeVariantContext = klassProperyNode
          .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
      while (attributeVariantContext.hasNext()) {
        contextVertex.add(attributeVariantContext.next());
      }
    }

    return contextVertex;
  }
  
  public static void prepareADMForEntity(List<Map<String, Object>> entityMap, List<Map<String, Object>> returnMap, List<String> entityList)
  {
    for (Map<String, Object> entity : entityMap) {
      Map<String, Object> tagValueMappings = new HashMap<>();
      String entityId = (String) entity.get(CommonConstants.CODE_PROPERTY);
      if (entityList.contains(entityId)) {
        Map<String, Object> mapToBeAdded = new HashMap<>();
        mapToBeAdded.put(IConfigRuleAttributeMappingModel.IS_IGNORED, false);
        mapToBeAdded.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID, entityId);
        mapToBeAdded.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, Arrays.asList(entityId));
        if (entity.containsKey(ITagInfoModel.CHILD_TAG)) {
          List<Map<String, Object>> mappingsList = new ArrayList<>();
          for (Map<String, Object> childTag : (List<Map<String, Object>>) entity.get(ITagInfoModel.CHILD_TAG)) {
            Map<String, Object> tagMappings = new HashMap<>();
            tagMappings.put(ITagValueMappingModel.IS_IGNORE_CASE, false);
            tagMappings.put(ITagValueMappingModel.MAPPED_TAG_VALUE_ID, childTag.get(CommonConstants.CODE_PROPERTY));
            tagMappings.put(ITagValueMappingModel.TAG_VALUE, childTag.get(CommonConstants.CODE_PROPERTY));
            mappingsList.add(tagMappings);
          }
          tagValueMappings.put(IColumnValueTagValueMappingModel.COLUMN_NAME, entityId);
          tagValueMappings.put(IColumnValueTagValueMappingModel.MAPPINGS, mappingsList);
          mapToBeAdded.put(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS, Arrays.asList(tagValueMappings));
        }
        returnMap.add(mapToBeAdded);
      }
    }
  }
  
  public static void checkAndAddMappings(String mappingId, IMappingHelperModel mappingHelperModel) throws Exception
  {
    try {
      Vertex mappingNode = UtilClass.getVertexByIndexedId(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
      IMappingConfigDetailsHelperModel configDetails = mappingHelperModel.getConfigDetails();
      
      Set<String> finalPropertyCollectionIds = new HashSet<>();
      Set<String> finalRelationshipsIds = new HashSet<>();
      Set<String> finalAttributeIds = new HashSet<>();
      Set<String> finalTagIds = new HashSet<>();
      Set<String> finalcontextIds = new HashSet<>();
      Set<String> finalcontextTagIds = new HashSet<>();
      
      Iterable<Vertex> klassIVertices = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE);
      Iterable<Vertex> taxonomyIVertices = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE);
      
      Set<String> klassAndTaxonomyINodes = new HashSet<>(UtilClass.getCodes(klassIVertices));
      klassAndTaxonomyINodes.addAll(UtilClass.getCodes(taxonomyIVertices));
      for (String klassINode : klassAndTaxonomyINodes) {
        getRelationshipIdsAndPCIdsFromINodeId(finalRelationshipsIds, finalPropertyCollectionIds, klassINode, finalAttributeIds,
            finalTagIds, finalcontextIds, finalcontextTagIds);
      }
      Map<String, Object> returnMap = new HashMap<>();
      //Get properties mapping from PCs
      getPropertiesMapping(mappingNode, mappingHelperModel, returnMap);
      Map<String, Object> pcTagsMap = new HashMap<>(configDetails.getTags());
      Set<String> currentPCTagIds = new HashSet<>(pcTagsMap.keySet());
      configDetails.getTags().clear();
      
    //Get tag mapping from contexts
      getContextTagMapping(mappingNode, mappingHelperModel, returnMap);
      Set<String> currentContextTagIds = new HashSet<>(configDetails.getTags().keySet());
      configDetails.getTags().putAll(pcTagsMap);
      
      getEntityMapping(mappingNode, RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE,
          mappingHelperModel.getConfigDetails().getRelationships());
      
      Set<String> currentPCIds = configDetails.getPropertyCollections().keySet();
      Set<String> currentAttributeIds = configDetails.getAttributes().keySet();
      Set<String> currentRelationshipIds = configDetails.getRelationships().keySet();
      Set<String> currentContextIds = configDetails.getContexts().keySet();
      
      //Manage added PCs in the config class
      Set<String> propertyCollectionToBeAdded = new HashSet<>(finalPropertyCollectionIds);
      propertyCollectionToBeAdded.removeAll(currentPCIds);
      for (String propertyCollectionId : propertyCollectionToBeAdded) {
        linkPropertyCollection(mappingNode, currentAttributeIds, currentPCTagIds,
            UtilClass.getVertexByCode(propertyCollectionId, VertexLabelConstants.PROPERTY_COLLECTION), propertyCollectionId);
      }
      
      //Manage added context in the config class
      Set<String> contextToBeAdded = new HashSet<>(finalcontextIds);
      contextToBeAdded.removeAll(currentContextIds);
      for (String contextID: contextToBeAdded) {
        linkContext(mappingNode, currentPCTagIds, currentContextTagIds, UtilClass.getVertexByCode(contextID, VertexLabelConstants.VARIANT_CONTEXT), contextID);
      }
      
      //Manage added relationship in the config class
      List<Map<String, Object>> addedRelationshipsMapping = new ArrayList<>();
      Set<String> relationshipToBeAdded = new HashSet<>(finalRelationshipsIds);
      relationshipToBeAdded.removeAll(currentRelationshipIds);
      for (String relationshipId : relationshipToBeAdded) {
        prepareADMForEntity(
            Arrays.asList(UtilClass.getMapFromVertex(FIELDS_TO_FETCH,
                UtilClass.getVertexByCode(relationshipId, VertexLabelConstants.ROOT_RELATIONSHIP))),
            addedRelationshipsMapping, Arrays.asList(relationshipId));
      }
      MappingUtils.saveRelationshipMapping(mappingNode, addedRelationshipsMapping, new ArrayList<>(), new ArrayList<>());
      
      Set<Vertex> relationshipsToDelete = new HashSet<>();
      Iterable<Vertex> relationshipINodes = mappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE);
      for (Vertex relationshipINode : relationshipINodes) {
        Iterable<Vertex> relationshipNodes = relationshipINode.getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY);
        if (relationshipNodes.iterator().hasNext()) {
          Vertex relationshipNode = relationshipNodes.iterator().next();
          if (!finalRelationshipsIds.contains(UtilClass.getCodeNew(relationshipNode))) {
            relationshipsToDelete.add(relationshipINode);
          }
        }
        else {
          relationshipsToDelete.add(relationshipINode);
        }
      }
      UtilClass.deleteVertices(relationshipsToDelete);
      
      Set<Vertex> PCsToDelete = new HashSet<>();
      Set<Vertex> contextToDelete = new HashSet<>();
      Set<Vertex> AttrsToDelete = new HashSet<>();
      Set<Vertex> TagsToDelete = new HashSet<>();
      
      finalTagIds.addAll(finalcontextTagIds);
      getVerticesToDelete(mappingNode, finalPropertyCollectionIds, finalAttributeIds, finalTagIds, PCsToDelete, AttrsToDelete,
          TagsToDelete, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
      
      getVerticesToDelete(mappingNode, finalcontextIds, finalAttributeIds, finalTagIds, contextToDelete, AttrsToDelete,
          TagsToDelete, RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE);
      
      UtilClass.deleteVertices(PCsToDelete);
      UtilClass.deleteVertices(AttrsToDelete);
      UtilClass.deleteVertices(TagsToDelete);
      UtilClass.deleteVertices(contextToDelete);
      
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
  }

  private static void getVerticesToDelete(Vertex mappingNode, Set<String> finalEntityIds, Set<String> finalAttributeIds,
      Set<String> finalTagIds, Set<Vertex> entityToDelete, Set<Vertex> AttrsToDelete, Set<Vertex> TagsToDelete, String relationshipLabel) throws Exception
  {
    Iterable<Vertex> PCINodes = mappingNode.getVertices(Direction.OUT, relationshipLabel);
    for (Vertex PCINode : PCINodes) {
      Iterable<Vertex> PCNodes = PCINode.getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY);
      if (PCNodes.iterator().hasNext()) {
        Vertex PCNode = PCNodes.iterator().next();
        List<String> PCAttrIds = new ArrayList<>();
        List<String> PCTagIds = new ArrayList<>();
        Map<String, Object> PCAttrTagMap = new HashMap<>();
        
        if(relationshipLabel.equals(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE)) {
          GetPropertyGroupInfo.getPropertyCollection(PCNode, PCAttrTagMap, PCAttrIds, PCTagIds);
        }else if(relationshipLabel.equals(RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE)) {
          GetPropertyGroupInfo.getContextTags(PCNode, PCAttrTagMap, PCTagIds);
        }
       
        String entityId = UtilClass.getCodeNew(PCNode);
        if (finalEntityIds.contains(entityId)) {
          
          if (!relationshipLabel.equals(RelationshipLabelConstants.HAS_CONTEXT_CONFIG_RULE)) {
            handlePropertiesToDelete(finalAttributeIds, AttrsToDelete, PCINode, PCAttrIds,
                RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
            List<String> pcAttrIdsToLink = new ArrayList<>(PCAttrIds);
            pcAttrIdsToLink.retainAll(finalAttributeIds);
            for (String pcAttrId : pcAttrIdsToLink) {
              linkPCIToEntityConfigRule(mappingNode, PCINode, pcAttrId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
                  RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, relationshipLabel);
            }
            PCAttrIds.removeAll(pcAttrIdsToLink);
          }
          
          handlePropertiesToDelete(finalTagIds, TagsToDelete, PCINode, PCTagIds, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
          List<String> pcTagIdsToLink = new ArrayList<>(PCTagIds);
          pcTagIdsToLink.retainAll(finalTagIds);
          for (String pcTagId : pcTagIdsToLink) {
            linkPCIToEntityConfigRule(mappingNode, PCINode, pcTagId, VertexLabelConstants.ENTITY_TAG,
                RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, relationshipLabel);
          }
          PCTagIds.removeAll(pcTagIdsToLink);
          
          //In case of context ignore attribute.
          if (relationshipLabel.equals(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE)) {
            List<Map<String, Object>> addedAttributesMapping = new ArrayList<>();
            prepareADMForEntity((List<Map<String, Object>>) PCAttrTagMap.get(IGetPropertyGroupInfoResponseModel.ATTRIBUTE_LIST),
                addedAttributesMapping, PCAttrIds);
            saveAttributeMapping(PCINode, addedAttributesMapping, new ArrayList<>());
          }
          
          List<Map<String, Object>> addedTagsMapping = new ArrayList<>();
          prepareADMForEntity((List<Map<String, Object>>) PCAttrTagMap.get(IGetPropertyGroupInfoResponseModel.TAG_LIST), addedTagsMapping,
              PCTagIds);
          saveTagMapping(PCINode, addedTagsMapping, new ArrayList<>());
          
        }
        else {
          entityToDelete.add(PCINode);
        }
      }
      else {
        entityToDelete.add(PCINode);
      }
    }
  }

  private static void handlePropertiesToDelete(Set<String> finalPropertiesIds, Set<Vertex> propertiesToDelete, Vertex mainEntityNode, List<String> mainEntityPropertiesIds, String relationLabel)
  {
    Iterable<Edge> propertyEdges = mainEntityNode.getEdges(Direction.OUT, relationLabel);
    for (Edge propertyEdge : propertyEdges) {
      Vertex propertyINode = propertyEdge.getVertex(Direction.IN);
      Iterable<Vertex> propertyNodes = propertyINode.getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY);
      if (propertyNodes.iterator().hasNext()) {
        Vertex propertyNode = propertyNodes.iterator().next();
        String propertyCode = UtilClass.getCodeNew(propertyNode);
          if (finalPropertiesIds.contains(propertyCode)) {
            if (!mainEntityPropertiesIds.contains(propertyCode)) {
              propertyEdge.remove();
            }
            else {
              mainEntityPropertiesIds.remove(propertyCode);
            }
          }
          else {
            propertiesToDelete.add(propertyINode);
          }
      }
      else {
        propertiesToDelete.add(propertyINode);
      }
    }
  }
  
}
