package com.cs.config.strategy.plugin.usecase.smartdocument.base;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleTags;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetTagRule;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SmartDocumentPresetUtils {
  
  public static final String[] fieldsToFetchKlasses    = { IIdLabelCodeModel.ID,
      IIdLabelCodeModel.CODE, IIdLabelCodeModel.LABEL, IKlass.CLASSIFIER_IID };
  
  public static final String[] fieldsToFetchTaxonomies = { IReferencedArticleTaxonomyModel.ID,
      IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.LABEL, ITaxonomy.CLASSIFIER_IID };
  
  public static final String[] fieldsToFetchLanguage   = { IIdLabelCodeModel.ID,
      IIdLabelCodeModel.CODE, IIdLabelCodeModel.LABEL};
  
  public static Map<String, Object> getSmartDocumentPresetById(String smartDocumentPresetId)
      throws Exception
  {
    Vertex smartDocumentVertex = UtilClass.getVertexById(smartDocumentPresetId,
        VertexLabelConstants.SMART_DOCUMENT_PRESET);
    
    Map<String, Object> returnMap = UtilClass.getMapFromNode(smartDocumentVertex);
    getMapToReturn(returnMap);
    fillAttributeRulesData(smartDocumentVertex, returnMap);
    fillTagRulesData(smartDocumentVertex, returnMap);
    fillReferencedInformation(smartDocumentVertex, returnMap);
    return returnMap;
  }
  
  private static void fillReferencedInformation(Vertex smartDocumentVertex,
      Map<String, Object> returnMap) throws Exception
  {
    // Attributes
    Map<String, Object> referencedAttributes = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_ATTRIBUTES);
    List<String> attributeIds = fillReferencedProperties(smartDocumentVertex,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_ATTRIBUTE_LINK, referencedAttributes, null);
    returnMap.put(ISmartDocumentPresetModel.ATTRIBUTE_IDS, attributeIds);
    
    // Tags
    Map<String, Object> referencedTags = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_TAGS);
    List<String> tagIds = fillReferencedProperties(smartDocumentVertex,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAG_LINK, referencedTags, null);
    returnMap.put(ISmartDocumentPresetModel.TAG_IDS, tagIds);
    
    // Klasses
    List<Long> klassClassifierIds = new ArrayList<>();
    Map<String, Object> referencedKlasses = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_KLASSES);
    List<String> klassIds = fillReferencedProperties(smartDocumentVertex,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_KLASS_LINK, referencedKlasses, klassClassifierIds,
        fieldsToFetchKlasses);
    returnMap.put(ISmartDocumentPresetModel.KLASS_IDS, klassIds);
    returnMap.put(ISmartDocumentPresetModel.KLASS_CLASSIFIER_IDS, klassClassifierIds);
    
    // Taxonomies
    List<Long> taxClassifierIds = new ArrayList<>();
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_TAXONOMIES);
    List<String> taxonomyIds = fillReferencedProperties(smartDocumentVertex,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAXONOMY_LINK, referencedTaxonomies,
        taxClassifierIds, fieldsToFetchTaxonomies);
    returnMap.put(ISmartDocumentPresetModel.TAXONOMY_IDS, taxonomyIds);
    returnMap.put(ISmartDocumentPresetModel.TAXONOMY_CLASSIFIER_IDS, taxClassifierIds);
    
    // Languages
    Map<String, Object> referencedLanguages = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_LANGUAGES);
    String languageCode = smartDocumentVertex.getProperty(ISmartDocumentPresetModel.LANGUAGE_CODE);
    if (languageCode != null && !languageCode.isEmpty()) {
      Vertex languageVertex = UtilClass.getVertexByCode(languageCode,
          VertexLabelConstants.LANGUAGE);
      Map<String, Object> propertyMap = UtilClass
          .getMapFromVertex(Arrays.asList(fieldsToFetchLanguage), languageVertex);
      referencedLanguages.put((String) propertyMap.get(IConfigEntity.CODE), propertyMap);
    }
  }
  
  private static List<String> fillReferencedProperties(Vertex smartDocumentVertex, String edgeLabel,
      Map<String, Object> referencedProperties, List<Long> klassClassifierIds, String... fieldsToFetch) throws Exception
  {
    List<String> propertyIds = new ArrayList<>();
    Iterable<Vertex> smartDocumentPresetLinkedProperties = smartDocumentVertex
        .getVertices(Direction.OUT, edgeLabel);
    
    for (Vertex propertyVertex : smartDocumentPresetLinkedProperties) {
      Map<String, Object> propertyMap = UtilClass.getMapFromVertex(Arrays.asList(fieldsToFetch),
          propertyVertex);
      String propertyId = (String) propertyMap.get(IEntity.ID);
      propertyIds.add(propertyId);
      if (klassClassifierIds != null) {
        klassClassifierIds.add(Long.parseLong(propertyMap.get(IKlass.CLASSIFIER_IID).toString()));
        propertyMap.remove(IKlass.CLASSIFIER_IID);
      }
      if (edgeLabel.equals(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAXONOMY_LINK)) {
        fillTaxonomiesChildrenAndParentData(propertyMap, propertyVertex);
      }
      referencedProperties.put(propertyId, propertyMap);
    }
    return propertyIds;
  }
  
  private static void fillTaxonomiesChildrenAndParentData(Map<String, Object> propertyMap,
      Vertex propertyVertex) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        CommonConstants.CODE_PROPERTY);
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    Iterable<Vertex> taxonomyChildren = propertyVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    taxonomyChildren.forEach(taxonomyChild -> {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, taxonomyChild));
    });
    propertyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap, propertyVertex);
    propertyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
  }
  
  public static void fillAttributeRulesData(Vertex smartDocumentPresetRule,
      Map<String, Object> returnMap)
  {
    List<Map<String, Object>> attributes = new ArrayList<>();
    Map<String, Object> referencedAttributes = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_ATTRIBUTES);
    Iterable<Vertex> attributeIntermediates = smartDocumentPresetRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_ATTRIBUTE);
    for (Vertex attributeIntermediate : attributeIntermediates) {
      Iterable<Vertex> attributeVertices = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_ATTR_LINK);
      Vertex attribute = attributeVertices.iterator()
          .next();
      Map<String, Object> attributeMap = UtilClass.getMapFromNode(attributeIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = attribute.getProperty(CommonConstants.CODE_PROPERTY);
      attributeMap.put(ISmartDocumentPresetRuleIntermediateEntity.ENTITY_ID, entityId);
      referencedAttributes.put(entityId, AttributeUtils.getAttributeMap(attribute));
      Iterable<Vertex> rules = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK);
      for (Vertex rule : rules) {
        rulesList.add(UtilClass.getMapFromNode(rule));
      }
      attributeMap.put(ISmartDocumentPresetRuleIntermediateEntity.RULES, rulesList);
      attributes.add(attributeMap);
    }
    returnMap.put(ISmartDocumentPresetModel.ATTRIBUTES, attributes);
  }
  
  public static void fillTagRulesData(Vertex smartDocumentPresetRule, Map<String, Object> returnMap)
      throws Exception
  {
    List<Map<String, Object>> tags = new ArrayList<>();
    Map<String, Object> referencedTags = (Map<String, Object>) returnMap
        .get(IGetSmartDocumentPresetModel.REFERENCED_TAGS);
    Iterable<Vertex> tagIntermediates = smartDocumentPresetRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG);
    for (Vertex tagIntermediate : tagIntermediates) {
      Iterable<Vertex> tagVertices = tagIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_LINK);
      Vertex tag = tagVertices.iterator()
          .next();
      Map<String, Object> tagMap = UtilClass.getMapFromNode(tagIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = tag.getProperty(CommonConstants.CODE_PROPERTY);
      tagMap.put(ISmartDocumentPresetRuleTags.ENTITY_ID, entityId);
      Iterable<Vertex> rules = tagIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK);
      referencedTags.put(entityId, TagUtils.getTagMap(tag, true));
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        List<Map<String, Object>> tagValues = new ArrayList<>();
        ruleMap.put(ISmartDocumentPresetTagRule.TAG_VALUES, tagValues);
        Iterable<Edge> ruleTagValueEdges = rule.getEdges(Direction.OUT,
            RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK);
        String type = (String) ruleMap.get(ISmartDocumentPresetTagRule.TYPE);
        if (!type.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            && !type.equals(CommonConstants.EMPTY_PROPERTY)) {
          for (Edge ruleTagValueEdge : ruleTagValueEdges) {
            Map<String, Object> tagValue = new HashMap<>();
            String tagId = ruleTagValueEdge.getProperty(IDataRuleTagValues.INNER_TAG_ID);
            tagValue.put(IDataRuleTagValues.TO,
                ruleTagValueEdge.getProperty(IDataRuleTagValues.TO));
            tagValue.put(IDataRuleTagValues.FROM,
                ruleTagValueEdge.getProperty(IDataRuleTagValues.FROM));
            tagValue.put(IDataRuleTagValues.ID, tagId);
            tagValues.add(tagValue);
          }
          ruleMap.put(ISmartDocumentPresetTagRule.TAG_VALUES, tagValues);
        }
        rulesList.add(ruleMap);
      }
      tagMap.put(ISmartDocumentPresetRuleTags.RULES, rulesList);
      tags.add(tagMap);
    }
    returnMap.put(ISmartDocumentPresetModel.TAGS, tags);
  }
  
  public static void getMapToReturn(Map<String, Object> mapToReturn)
  {
    mapToReturn.put(IGetSmartDocumentPresetModel.REFERENCED_ATTRIBUTES, new HashMap<>());
    mapToReturn.put(IGetSmartDocumentPresetModel.REFERENCED_KLASSES, new HashMap<>());
    mapToReturn.put(IGetSmartDocumentPresetModel.REFERENCED_TAGS, new HashMap<>());
    mapToReturn.put(IGetSmartDocumentPresetModel.REFERENCED_TAXONOMIES, new HashMap<>());
    mapToReturn.put(IGetSmartDocumentPresetModel.REFERENCED_LANGUAGES, new HashMap<>());
  }
}
