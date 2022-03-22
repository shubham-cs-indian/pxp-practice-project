package com.cs.config.strategy.plugin.usecase.datarule.util;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.attribute.util.GetGridAttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.*;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.datarule.*;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.endpoint.EndpointNotFoundException;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.datarule.*;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.*;

@SuppressWarnings("unchecked")
public class GetDataRuleUtils {
  
  public static Map<String, Object> getDataRuleFromNode(Vertex dataRule) throws Exception
  {
    return getDataRuleFromNode(dataRule, false);
  }
  
  public static Map<String, Object> getDataRuleFromNode(Vertex dataRule, Boolean getReferencedData)
      throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(CommonConstants.ID_PROPERTY,
        dataRule.getProperty(CommonConstants.CODE_PROPERTY));
    returnMap.put(CommonConstants.LABEL_PROPERTY,
        UtilClass.getValueByLanguage(dataRule, CommonConstants.LABEL_PROPERTY));
    returnMap.put(CommonConstants.CODE_PROPERTY,
        dataRule.getProperty(CommonConstants.CODE_PROPERTY));
    
    returnMap = UtilClass.getMapFromNode(dataRule);
    List<Map<String, Object>> attributes = new ArrayList<>();
    List<Map<String, Object>> referencedRuleList = new ArrayList<>();
    List<Map<String, Object>> relationships = new ArrayList<>();
    List<Map<String, Object>> roles = new ArrayList<>();
    List<Map<String, Object>> tags = new ArrayList<>();
    List<String> klassIds = new ArrayList<>();
    List<Map<String, Object>> ruleViolations = new ArrayList<>();
    List<Map<String, Object>> normalizations = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<String> taxonomies = new ArrayList<>();
    List<String> organizations = new ArrayList<>();
    List<String> endpoints = new ArrayList<>();
    List<String> languages = new ArrayList<>();
    
    fillAttributeRulesData(dataRule, attributes, referencedRuleList, getReferencedData);
    fillRuleViolationsData(dataRule, ruleViolations);
    fillRoleRulesData(dataRule, roles);
    // fillTypeRulesData(dataRule, types);
    fillRelationshipRulesData(dataRule, relationships);
    fillTagRulesData(dataRule, tags);
    fillNormalizationData(dataRule, normalizations);
    fillTypesData(dataRule, types);
    fillTaxonomiesData(dataRule, taxonomies);
    fillOrganizations(dataRule, organizations);
    fillEndpoints(dataRule, endpoints);
    fillLanguages(dataRule, languages);
    
    returnMap.put("attributes", attributes);
    returnMap.put("referencedRuleList", referencedRuleList);
    returnMap.put("relationships", relationships);
    returnMap.put("roles", roles);
    returnMap.put("types", types);
    returnMap.put("tags", tags);
    returnMap.put("klassIds", klassIds);
    returnMap.put("ruleViolations", ruleViolations);
    returnMap.put("normalizations", normalizations);
    returnMap.put("taxonomies", taxonomies);
    returnMap.put(IDataRule.ENDPOINTS, endpoints);
    returnMap.put(IDataRule.ORGANIZATIONS, organizations);
    returnMap.put(IDataRule.LANGUAGES, languages);
    
    return returnMap;
  }
  
  public static void fillLanguages(Vertex dataRule, List<String> languages)
  {
    Iterable<Vertex> languageNodes = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_LANGUAGE_LINK);
    for (Vertex languageNode : languageNodes) {
      languages.add(UtilClass.getCode(languageNode));
    }
  }
  
  public static void fillEndpoints(Vertex dataRule, List<String> endpoints)
  {
    Iterable<Vertex> endpointVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_ENDPOINT_LINK);
    for (Vertex endpointvertex : endpointVertices) {
      endpoints.add(UtilClass.getCodeNew(endpointvertex));
    }
  }
  
  public static void fillOrganizations(Vertex dataRule, List<String> organizations)
  {
    Iterable<Vertex> orgNodes = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANISATION_RULE_LINK);
    for (Vertex orgNode : orgNodes) {
      organizations.add(UtilClass.getCodeNew(orgNode));
    }
  }
  
  public static void fillAttributeRulesData(Vertex dataRule, List<Map<String, Object>> attributes,
      List<Map<String, Object>> referencedRuleList, Boolean getReferencedData)
  {
    Iterable<Vertex> attributeIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
    for (Vertex attributeIntermediate : attributeIntermediates) {
      Iterable<Vertex> attributeVertices = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK);
      Vertex attribute = attributeVertices.iterator()
          .next();
      Map<String, Object> attributeMap = UtilClass.getMapFromNode(attributeIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = attribute.getProperty(CommonConstants.CODE_PROPERTY);
      attributeMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        String ruleListLinkId = null;
        String attributeLinkId = null;
        Iterable<Vertex> ruleListVertices = rule.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_RULE_LIST);
        Iterable<Vertex> linkedAttributeVertices = rule.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_ATTRIBUTE_LINK);
        Iterator<Vertex> linkedKlassVertices = rule
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_LINK)
            .iterator();

        List<String> ruleListValues = new ArrayList<>();
        Set<String> klassLinkIds = new HashSet<>();
        if (ruleListVertices.iterator()
            .hasNext()) {
          Vertex ruleList = ruleListVertices.iterator()
              .next();
          ruleListLinkId = ruleList.getProperty(CommonConstants.CODE_PROPERTY);
          Map<String, Object> ruleListMap = UtilClass.getMapFromNode(ruleList);
          ruleListValues.addAll((List<String>) ruleListMap.get(CommonConstants.LIST_PROPERTY));
          referencedRuleList.add(ruleListMap);
        }

        if (linkedAttributeVertices.iterator()
            .hasNext()) {
          Vertex linkedAttribute = linkedAttributeVertices.iterator()
              .next();
          attributeLinkId = linkedAttribute.getProperty(CommonConstants.CODE_PROPERTY);
        }
        while (linkedKlassVertices.hasNext()) {
          Vertex klassNode = linkedKlassVertices.next();
          String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
          if (!klassLinkIds.contains(klassId)) {
            if (getReferencedData) {
              klassLinkIds.addAll(getAllChildrenKlassIds(klassNode));
            }
            klassLinkIds.add(klassId);
          }
        }

        ruleMap.put(CommonConstants.RULE_LIST_LINK_ID_PROPERTY, ruleListLinkId);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, attributeLinkId);
        ruleMap.put(IDataRuleEntityRule.KLASS_LINK_IDS, klassLinkIds);
        if (!ruleListValues.isEmpty()) {
          ruleMap.put(CommonConstants.VALUES_PROPERTY, ruleListValues);
        }
        rulesList.add(ruleMap);
      }
      attributeMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      attributes.add(attributeMap);
    }
  }

  public static void fillRuleViolationsData(Vertex dataRule,
      List<Map<String, Object>> ruleViolationList)
  {
    
    Iterable<Vertex> ruleViolationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_VIOLATION_LINK);
    for (Vertex ruleViolation : ruleViolationVertices) {
      Map<String, Object> ruleViolationMap = new HashMap<>();
      ruleViolationMap = UtilClass.getMapFromNode(ruleViolation);
      ruleViolationList.add(ruleViolationMap);
    }
  }
  
  public static void fillRoleRulesData(Vertex dataRule, List<Map<String, Object>> roles)
  {
    Iterable<Vertex> roleIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.ROLE_DATA_RULE);
    for (Vertex roleIntermediate : roleIntermediates) {
      Iterable<Vertex> roleVertices = roleIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.ROLE_DATA_RULE_LINK);
      Vertex role = roleVertices.iterator()
          .next();
      Map<String, Object> roleMap = UtilClass.getMapFromNode(roleIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = role.getProperty(CommonConstants.CODE_PROPERTY);
      roleMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = roleIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        List<String> values = new ArrayList<>();
        Iterable<Vertex> users = rule.getVertices(Direction.OUT,
            RelationshipLabelConstants.RULE_USER_LINK);
        for (Vertex user : users) {
          String userId = user.getProperty(CommonConstants.CODE_PROPERTY);
          values.add(userId);
        }
        ruleMap.put(CommonConstants.VALUES_PROPERTY, values);
        ruleMap.put(CommonConstants.RULE_LIST_LINK_ID_PROPERTY, null);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, null);
        rulesList.add(ruleMap);
      }
      roleMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      roles.add(roleMap);
    }
  }
  
  /* public static void fillTypeRulesData(Vertex dataRule, List<Map<String, Object>> types)
  {
    Iterable<Vertex> klassIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.TYPE_DATA_RULE);
    for (Vertex klassIntermediate : klassIntermediates) {
      Iterable<Vertex> klassVertices = klassIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.KLASS_DATA_RULE_LINK);
      Vertex klass = klassVertices.iterator()
          .next();
      Map<String, Object> klassMap = UtilClass.getMapFromNode(klassIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = klass.getProperty(CommonConstants.CODE_PROPERTY);
      klassMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = klassIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        ruleMap.put(CommonConstants.RULE_LIST_LINK_ID_PROPERTY, null);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, null);
        rulesList.add(ruleMap);
      }
      klassMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      types.add(klassMap);
    }
  }*/
  public static void fillTypesData(Vertex dataRule, List<String> types)
  {
    Iterable<Vertex> klassVertices = dataRule.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
    for (Vertex klass : klassVertices) {
      types.add(UtilClass.getCodeNew(klass));
    }
    /* Iterator<Vertex> klassIntermediates = dataRule.getVertices(Direction.OUT,RelationshipLabelConstants.TYPE_DATA_RULE).iterator();
    if(klassIntermediates.hasNext()) {
      Vertex klassIntermediate = klassIntermediates.next();
      Iterable<Vertex> klassVertices = klassIntermediate.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_LINK);
      for (Vertex klass : klassVertices) {
        types.add(UtilClass.getCode(klass));
      }
    }*/
  }
  
  public static void fillTaxonomiesData(Vertex dataRule, List<String> taxonomies)
  {
    Iterable<Vertex> taxonomyVertices = dataRule.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
    for (Vertex taxonomy : taxonomyVertices) {
      taxonomies.add(UtilClass.getCodeNew(taxonomy));
    }
    
    /* Iterator<Vertex> taxonomyIntermediates = dataRule.getVertices(Direction.OUT,RelationshipLabelConstants.TAXONOMY_DATA_RULE).iterator();
    if(taxonomyIntermediates.hasNext()) {
      Vertex taxonomyIntermediate = taxonomyIntermediates.next();
      Iterable<Vertex> taxonomyVertices = taxonomyIntermediate.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TAXONOMY_LINK);
      for (Vertex taxonomy : taxonomyVertices) {
        taxonomies.add(UtilClass.getCode(taxonomy));
      }
    }*/
  }
  
  public static void fillRelationshipRulesData(Vertex dataRule,
      List<Map<String, Object>> relationships)
  {
    Iterable<Vertex> relationshipIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIP_DATA_RULE);
    for (Vertex relationshipIntermediate : relationshipIntermediates) {
      Iterable<Vertex> relationshipVertices = relationshipIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIP_DATA_RULE_LINK);
      Vertex relationship = relationshipVertices.iterator()
          .next();
      Map<String, Object> relationshipMap = UtilClass.getMapFromNode(relationshipIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = relationship.getProperty(CommonConstants.CODE_PROPERTY);
      relationshipMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = relationshipIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        ruleMap.put(CommonConstants.RULE_LIST_LINK_ID_PROPERTY, null);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, null);
        rulesList.add(ruleMap);
      }
      relationshipMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      relationships.add(relationshipMap);
    }
  }
  
  public static void fillTagRulesData(Vertex dataRule, List<Map<String, Object>> tags)
  {
    Iterable<Vertex> tagIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.TAG_DATA_RULE);
    for (Vertex tagIntermediate : tagIntermediates) {
      Iterable<Vertex> tagVertices = tagIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.TAG_DATA_RULE_LINK);
      Vertex tag = tagVertices.iterator()
          .next();
      Map<String, Object> tagMap = UtilClass.getMapFromNode(tagIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = tag.getProperty(CommonConstants.CODE_PROPERTY);
      tagMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = tagIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        List<Map<String, Object>> tagValues = new ArrayList<>();
        ruleMap.put(CommonConstants.TAG_VALUES, tagValues);
        Iterable<Edge> ruleTagValueEdges = rule.getEdges(Direction.OUT,
            RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
        String type = (String) ruleMap.get(CommonConstants.TYPE_PROPERTY);
        if (!type.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            && !type.equals(CommonConstants.EMPTY_PROPERTY)) {
          for (Edge ruleTagValueEdge : ruleTagValueEdges) {
            Map<String, Object> tagValue = new HashMap<>();
            int to = ruleTagValueEdge.getProperty(CommonConstants.TO_PROPERTY);
            int from = ruleTagValueEdge.getProperty(CommonConstants.FROM_PROPERTY);
            String tagId = ruleTagValueEdge.getProperty(CommonConstants.ENTITY_ID_PROPERTY);
            tagValue.put(CommonConstants.TO_PROPERTY, to);
            tagValue.put(CommonConstants.FROM_PROPERTY, from);
            tagValue.put(CommonConstants.ID_PROPERTY, tagId);
            tagValues.add(tagValue);
          }
          ruleMap.put(CommonConstants.TAG_VALUES, tagValues);
        }
        rulesList.add(ruleMap);
      }
      tagMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      tags.add(tagMap);
    }
  }
  
  public static void fillNormalizationData(Vertex dataRule,
      List<Map<String, Object>> normalizations) throws Exception
  {

    Iterable<Vertex> normalizationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.NORMALIZATION_LINK);
    for (Vertex normalization : normalizationVertices) {
      Map<String, Object> normalizationMap = UtilClass.getMapFromNode(normalization);
      String type = normalization.getProperty(CommonConstants.TYPE_PROPERTY);
      if (type.equals(CommonConstants.TAG_PROPERTY)) {
        List<Map<String, Object>> tagValues = new ArrayList<>();
        Iterable<Vertex> tags = normalization.getVertices(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        Vertex tag = tags.iterator()
            .next();
        String tagType = tag.getProperty(CommonConstants.TAG_TYPE_PROPERTY);
        Iterable<Edge> tagValueEdges = normalization.getEdges(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
        for (Edge tagValueEdge : tagValueEdges) {
          Map<String, Object> tagValue = new HashMap<>();
          int to = tagValueEdge.getProperty(CommonConstants.TO_PROPERTY);
          int from = tagValueEdge.getProperty(CommonConstants.FROM_PROPERTY);
          String innerTagId = tagValueEdge.getProperty(IDataRuleTagValues.INNER_TAG_ID);
          String id = tagValueEdge.getProperty(CommonConstants.ENTITY_ID_PROPERTY);
          tagValue.put(CommonConstants.TO_PROPERTY, to);
          tagValue.put(CommonConstants.FROM_PROPERTY, from);
          tagValue.put(CommonConstants.ID_PROPERTY, id);
          tagValue.put(IDataRuleTagValues.INNER_TAG_ID, innerTagId);
         // tagValue.put(CommonConstants.CODE_PROPERTY, UtilClass.getCode(tagValueEdge.getVertex(Direction.IN)));
          tagValues.add(tagValue);
          if (to == 0 && from == 0) {
            if (tagType.equals(CommonConstants.YES_NEUTRAL_TAG_TYPE_ID)) {
              tagValues.remove(tagValue);
            }
          }
        }

        normalizationMap.put(CommonConstants.TAG_VALUES, tagValues);
      }
      else if (type.equals(CommonConstants.ATTRIBUTE)) {
        String transformationType = normalization.getProperty(INormalization.TRANSFORMATION_TYPE);
        if (transformationType.equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
          fillAttributeValueIdForNormalization(normalization, normalizationMap);
        }
        else if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
          fillConcatenatedListForNormalization(normalization, normalizationMap);
        }
      }
      else if (type.equals(CommonConstants.ROLE_PROPERTY)) {
        List<String> values = new ArrayList<>();
        Iterable<Vertex> users = normalization.getVertices(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_USER_LINK);
        for (Vertex user : users) {
          String userId = user.getProperty(CommonConstants.CODE_PROPERTY);
          values.add(userId);
        }
        normalizationMap.put(CommonConstants.VALUES_PROPERTY, values);
      }
      else if (type.equals("type") || type.equals("taxonomy")) {
        List<String> values = new ArrayList<>();
        Iterable<Vertex> entityVertices = normalization.getVertices(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        for (Vertex entity : entityVertices) {
          values.add(UtilClass.getCodeNew(entity));
        }
        normalizationMap.put(CommonConstants.VALUES_PROPERTY, values);
      }
      normalizations.add(normalizationMap);
    }
  }

  private static void fillConcatenatedListForNormalization(Vertex normalization,
      Map<String, Object> normalizationMap) throws Exception
  {
    Iterable<Vertex> concatenatedNodes = normalization.getVertices(Direction.OUT,
        new String[] { RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK,
            RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK,
            RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK });
    List<Map<String, Object>> concatenatedList = new ArrayList<>();
    for (Vertex concatenatedNode : concatenatedNodes) {
      Map<String, Object> concatenatedMap = UtilClass.getMapFromNode(concatenatedNode);
      String operatorType = (String) concatenatedMap.get(IConcatenatedOperator.TYPE);
      if (operatorType.equals(CommonConstants.ATTRIBUTE)) {
        Iterator<Vertex> attributeIterator = concatenatedNode
            .getVertices(Direction.OUT,
                RelationshipLabelConstants.NORMALIZATION_CONCATENATED_NODE_ATTRIBUTE_LINK)
            .iterator();
        concatenatedMap.put(IConcatenatedAttributeOperator.ATTRIBUTE_ID,
            UtilClass.getCodeNew(UtilClass.getVertexFromIterator(attributeIterator, true)));
        
      }
      else if (operatorType.equals(CommonConstants.TAG)) {
        Iterator<Vertex> tagIterator = concatenatedNode
            .getVertices(Direction.OUT,
                RelationshipLabelConstants.NORMALIZATION_CONCATENATED_NODE_TAG_LINK)
            .iterator();
        concatenatedMap.put(IConcatenatedTagOperator.TAG_ID,
            UtilClass.getCodeNew(UtilClass.getVertexFromIterator(tagIterator, true)));
      }
      concatenatedList.add(concatenatedMap);
    }
    List<Map<String, Object>> sortedConcatenatedList = AttributeUtils
        .getSortedAttributeConcatenatedList(concatenatedList);
    normalizationMap.put(IConcatenatedNormalization.ATTRIBUTE_CONCATENATED_LIST,
        sortedConcatenatedList);
  }
  
  private static void fillAttributeValueIdForNormalization(Vertex normalization,
      Map<String, Object> normalizationMap) throws Exception
  {
    
    Vertex valueAttributeVertex = DataRuleUtils.getAttributeNodeFromNormalization(normalization);
    normalizationMap.put(IAttributeValueNormalization.VALUE_ATTRIBUTE_ID,
        UtilClass.getCodeNew(valueAttributeVertex));
  }
  
  public static List<String> getAllChildrenKlassIds(Vertex klassNode)
  {
    List<String> childrenIds = new ArrayList<>();
    Iterable<Vertex> childrenVertices = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childKlass : childrenVertices) {
      childrenIds.add(childKlass.getProperty(CommonConstants.CODE_PROPERTY));
      childrenIds.addAll(getAllChildrenKlassIds(childKlass));
    }
    return childrenIds;
  }
  
  public static void fillConfigDetails(Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedKlasses = new HashMap<>();
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) returnMap
        .get(IDataRuleModel.ATTRIBUTES);
    for (Map<String, Object> attribute : attributes) {
      String attributeId = (String) attribute.get(IDataRuleIntermediateEntitys.ENTITY_ID);
      List<Map<String, Object>> rules = (List<Map<String, Object>>) attribute
          .get(IDataRuleIntermediateEntitys.RULES);
      for (Map<String, Object> rule : rules) {
        String attributeLinkId = (String) rule.get(IDataRuleEntityRule.ATTRIBUTE_LINK_ID);
        /*
         * null check because when attribute is not selected by default it's value is null
         * @Author: Abhaypratap Singh
         * */
        if (attributeLinkId != null) {
          referencedAttributes.put(attributeLinkId, getReferencedAttribute(attributeLinkId));
        }
      }
      referencedAttributes.put(attributeId, getReferencedAttribute(attributeId));
    }
    
    Map<String, Object> referencedTags = new HashMap<>();
    List<Map<String, Object>> tags = (List<Map<String, Object>>) returnMap.get(IDataRuleModel.TAGS);
    for (Map<String, Object> tag : tags) {
      String tagId = (String) tag.get(IDataRuleIntermediateEntitys.ENTITY_ID);
      referencedTags.put(tagId, getReferencedTag(tagId));
    }
    
    Map<String, Object> referencedRoles = new HashMap<>();
    List<Map<String, Object>> roles = (List<Map<String, Object>>) returnMap
        .get(IDataRuleModel.ROLES);
    for (Map<String, Object> role : roles) {
      String roleId = (String) role.get(IDataRuleIntermediateEntitys.ENTITY_ID);
      referencedRoles.put(roleId, getReferencedRole(roleId));
    }
    List<String> klassIds = (List<String>) returnMap.get("types");
    for (String klassId : klassIds) {
      referencedKlasses.put(klassId, getReferencedKlass(klassId));
    }
    
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    List<String> taxonomyIds = (List<String>) returnMap.get("taxonomies");
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyNode = UtilClass.getVertexById(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      Map<String, Object> taxonomyMap = new HashMap<>();
      TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyNode);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
    }
    
    List<Map<String, Object>> normalizations = (List<Map<String, Object>>) returnMap
        .get(IDataRuleModel.NORMALIZATIONS);
    List<Map<String, Object>> ruleViolations = (List<Map<String, Object>>) returnMap
        .get(IDataRuleModel.RULE_VIOLATIONS);
    List<Map<String, Object>> effects = new ArrayList<>();
    effects.addAll(normalizations);
    effects.addAll(ruleViolations);
    for (Map<String, Object> effect : effects) {
      String entityId = (String) effect.get(INormalization.ENTITY_ID);
      String type = (String) effect.get(INormalization.TYPE);
      switch (type) {
        case CommonConstants.ATTRIBUTE:
          Map<String, Object> referencedAttribute = getReferencedAttribute(entityId);
          referencedAttributes.put(entityId, referencedAttribute);
          if (referencedAttribute.get(IAttribute.TYPE)
              .equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
            List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) referencedAttribute
                .get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
            for (Map<String, Object> attributeOperator : attributeOperatorList) {
              String attributeOperatorId = (String) attributeOperator
                  .get(IAttributeOperator.ATTRIBUTE_ID);
              if (!UtilClass.isStringNullOrEmpty(attributeOperatorId)
                  && !referencedAttributes.containsKey(attributeOperatorId)) {
                referencedAttributes.put(attributeOperatorId,
                    getReferencedAttribute(attributeOperatorId));
              }
            }
          }
          else if (referencedAttribute.get(IAttribute.TYPE)
              .equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
            List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) referencedAttribute
                .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
            for (Map<String, Object> attributeOperator : attributeOperatorList) {
              String attributeOperatorId = (String) attributeOperator
                  .get(IAttributeOperator.ATTRIBUTE_ID);
              if (!UtilClass.isStringNullOrEmpty(attributeOperatorId)
                  && !referencedAttributes.containsKey(attributeOperatorId)) {
                referencedAttributes.put(attributeOperatorId,
                    getReferencedAttribute(attributeOperatorId));
              }
            }
          }
          String transformationType = (String) effect.get(INormalization.TRANSFORMATION_TYPE);
          if (transformationType != null) {
            if (transformationType.equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
              String valueAttributeId = (String) effect
                  .get(IAttributeValueNormalization.VALUE_ATTRIBUTE_ID);
              if (!referencedAttributes.containsKey(valueAttributeId)) {
                referencedAttributes.put(valueAttributeId,
                    getReferencedAttribute(valueAttributeId));
              }
              
            }
            else if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
              List<Map<String, Object>> concatenatedOperatorList = (List<Map<String, Object>>) effect
                  .get(IConcatenatedNormalization.ATTRIBUTE_CONCATENATED_LIST);
              GetGridAttributeUtils.fillReferencedAttributes(concatenatedOperatorList,
                  referencedAttributes);
              GetGridAttributeUtils.fillReferencedTags(concatenatedOperatorList, referencedTags);
            }
          }
          break;
        
        case CommonConstants.TAG:
          referencedTags.put(entityId, getReferencedTag(entityId));
          break;
        
        case CommonConstants.ROLE:
          referencedRoles.put(entityId, getReferencedRole(entityId));
          break;
        case "type":
          List<String> typeIds = (List<String>) effect.get(INormalization.VALUES);
          for (String klassId : typeIds) {
            referencedKlasses.put(klassId, getReferencedKlass(klassId));
          }
          break;
        
        case "taxonomy":
          List<String> taxonomies = (List<String>) effect.get(INormalization.VALUES);
          for (String taxonomyId : taxonomies) {
            Vertex taxonomyNode = UtilClass.getVertexById(taxonomyId,
                VertexLabelConstants.ROOT_KLASS_TAXONOMY);
            Map<String, Object> taxonomyMap = new HashMap<>();
            TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyNode);
            referencedTaxonomies.put(taxonomyId, taxonomyMap);
          }
          break;
      }
    }
    
    Map<String, Object> referencedOrganizations = new HashMap<>();
    List<String> organizations = (List<String>) returnMap.get(IDataRule.ORGANIZATIONS);
    for (String organization : organizations) {
      referencedOrganizations.put(organization, getReferencedOrganization(organization));
    }
    
    Map<String, Object> referencedEndpoints = new HashMap<>();
    List<String> endpoints = (List<String>) returnMap.get(IDataRule.ENDPOINTS);
    for (String endpoint : endpoints) {
      referencedEndpoints.put(endpoint, getReferencedEndpoint(endpoint));
    }
    
    Map<String, Object> referencedLanguges = new HashMap<>();
    List<String> languages = (List<String>) returnMap.get(IDataRule.LANGUAGES);
    for (String language : languages) {
      referencedLanguges.put(language, getReferencedLanguages(language));
    }
    
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_TAGS, referencedTags);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_ROLES, referencedRoles);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_KLASSES, referencedKlasses);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_ORANIZATIONS,
        referencedOrganizations);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_ENDPOINTS, referencedEndpoints);
    configDetails.put(IConfigDetailsForDataRuleModel.REFERENCED_LANGUAGES, referencedLanguges);
    returnMap.put(IDataRuleModel.CONFIG_DETAILS, configDetails);
  }
  
  private static Map<String, Object> getReferencedEndpoint(String endpoint) throws Exception
  {
    final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IEndpoint.LABEL);
    Vertex endpointNode;
    Map<String, Object> mapFromNode = new HashMap<>();
    try {
      endpointNode = UtilClass.getVertexByIndexedId(endpoint, VertexLabelConstants.ENDPOINT);
      mapFromNode = UtilClass.getMapFromVertex(fieldsToFetch, endpointNode);
    }
    catch (NotFoundException e) {
      throw new EndpointNotFoundException();
    }
    return mapFromNode;
  }
  
  private static Map<String, Object> getReferencedOrganization(String organization) throws Exception
  {
    final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IOrganization.LABEL);
    
    Vertex organizationNode;
    Map<String, Object> mapFromNode = new HashMap<>();
    try {
      organizationNode = UtilClass.getVertexByIndexedId(organization,
          VertexLabelConstants.ORGANIZATION);
      mapFromNode = UtilClass.getMapFromVertex(fieldsToFetch, organizationNode);
    }
    catch (NotFoundException e) {
      throw new OrganizationNotFoundException();
    }
    return mapFromNode;
  }
  
  public static Map<String, Object> getReferencedLanguages(String language) throws Exception
  {
    final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ILanguage.LABEL, ILanguage.CODE);
    
    Vertex languageNode;
    Map<String, Object> mapFromNode = new HashMap<>();
    try {
      languageNode = UtilClass.getVertexByCode(language, VertexLabelConstants.LANGUAGE);
      mapFromNode = UtilClass.getMapFromVertex(fieldsToFetch, languageNode);
    }
    catch (NotFoundException e) {
      throw new LanguageNotFoundException();
    }
    return mapFromNode;
  }
  
  private static Map<String, Object> getReferencedAttribute(String attributeId) throws Exception
  {
    Vertex attributeNode = null;
    try {
      attributeNode = UtilClass.getVertexById(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    }
    catch (NotFoundException e) {
      throw new AttributeNotFoundException();
    }
    Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
    return referencedAttribute;
  }
  
  private static Map<String, Object> getReferencedTag(String tagId) throws Exception
  {
    Vertex tagNode;
    try {
      tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    return TagUtils.getTagMap(tagNode, true);
  }
  
  private static Map<String, Object> getReferencedRole(String roleId) throws Exception
  {
    Vertex roleNode;
    try {
      roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    }
    catch (NotFoundException e) {
      throw new RoleNotFoundException();
    }
    return RoleUtils.getRoleEntityMap(roleNode);
  }
  
  private static Map<String, Object> getReferencedKlass(String klassId) throws Exception
  {
    Vertex klassNode;
    try {
      klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    return UtilClass.getMapFromVertex(Arrays.asList(IKlass.TYPE, IKlass.LABEL, IKlass.CODE, IKlass.IS_NATURE, IKlass.ICON), klassNode);
  }

  public static Map<String, Object> getAttributesForRules(Vertex dataRule)
  {
    Map<String, Object> attributes = new HashMap<>();
    Iterable<Vertex> attributeIntermediates = dataRule.getVertices(Direction.OUT, RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
    for (Vertex attributeIntermediate : attributeIntermediates) {
      Iterable<Vertex> attributeVertices = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK);
      Vertex attribute = attributeVertices.iterator().next();
      Map<String, Object> attributeMap = UtilClass.getMapFromNode(attributeIntermediate);
      Map<String, Map<String, Object>> rules = new HashMap<>();
      String entityId = attribute.getProperty(CommonConstants.CODE_PROPERTY);
      attributeMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> ruleNodes = attributeIntermediate.getVertices(Direction.OUT, RelationshipLabelConstants.RULE_LINK);

      for (Vertex rule : ruleNodes) {
        Map<String, Object> ruleMap = UtilClass.getMapFromNode(rule);

        String ruleListLinkId = null;
        String attributeLinkId = null;
        Iterable<Vertex> ruleLists = rule.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_RULE_LIST);
        Iterable<Vertex> linkedAttributes = rule.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_ATTRIBUTE_LINK);

        if (ruleLists.iterator().hasNext()) {
          Vertex ruleList = ruleLists.iterator().next();
          ruleListLinkId = ruleList.getProperty(CommonConstants.CODE_PROPERTY);
        }

        if (linkedAttributes.iterator().hasNext()) {
          Vertex linkedAttribute = linkedAttributes.iterator().next();
          attributeLinkId = linkedAttribute.getProperty(CommonConstants.CODE_PROPERTY);
        }

        ruleMap.put(CommonConstants.RULE_LIST_LINK_ID_PROPERTY, ruleListLinkId);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, attributeLinkId);
        rules.put((String) ruleMap.get(CommonConstants.CODE_PROPERTY), ruleMap);
      }
      attributeMap.put(CommonConstants.RULES_PROPERTY, rules);
      attributes.put(entityId, attributeMap);
    }
    return attributes;
  }

  public static Map<String, Object> getTagRules(Vertex dataRule)
  {
    Map<String, Object> tags = new HashMap<>();
    Iterable<Vertex> tagIntermediates = dataRule.getVertices(Direction.OUT, RelationshipLabelConstants.TAG_DATA_RULE);
    for (Vertex tagIntermediate : tagIntermediates) {
      Iterable<Vertex> tagVertices = tagIntermediate.getVertices(Direction.OUT, RelationshipLabelConstants.TAG_DATA_RULE_LINK);
      Vertex tag = tagVertices.iterator().next();
      Map<String, Object> tagMap = UtilClass.getMapFromNode(tagIntermediate);
      Map<String, Object> rulesList = new HashMap<>();
      String entityId = tag.getProperty(CommonConstants.CODE_PROPERTY);
      tagMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = tagIntermediate.getVertices(Direction.OUT, RelationshipLabelConstants.RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        List<Map<String, Object>> tagValues = new ArrayList<>();
        ruleMap.put(CommonConstants.TAG_VALUES, tagValues);
        Iterable<Edge> ruleTagValueEdges = rule.getEdges(Direction.OUT, RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
        String type = (String) ruleMap.get(CommonConstants.TYPE_PROPERTY);
        if (!type.equals(CommonConstants.NOT_EMPTY_PROPERTY) && !type.equals(CommonConstants.EMPTY_PROPERTY)) {
          for (Edge ruleTagValueEdge : ruleTagValueEdges) {
            Map<String, Object> tagValue = new HashMap<>();
            int to = ruleTagValueEdge.getProperty(CommonConstants.TO_PROPERTY);
            int from = ruleTagValueEdge.getProperty(CommonConstants.FROM_PROPERTY);
            String tagId = ruleTagValueEdge.getProperty(CommonConstants.ENTITY_ID_PROPERTY);
            tagValue.put(CommonConstants.TO_PROPERTY, to);
            tagValue.put(CommonConstants.FROM_PROPERTY, from);
            tagValue.put(CommonConstants.ID_PROPERTY, tagId);
            tagValues.add(tagValue);
          }
          ruleMap.put(CommonConstants.TAG_VALUES, tagValues);
        }
        rulesList.put((String) ruleMap.get(CommonConstants.CODE_PROPERTY), ruleMap);
      }
      tagMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      tags.put(entityId, tagMap);
    }
    return tags;
  }

  public static Map<String, Object> getRuleViolationsForRule(Vertex dataRule)
  {
    Map<String, Object> ruleViolations = new HashMap<>();
    Iterable<Vertex> ruleViolationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_VIOLATION_LINK);
    for (Vertex ruleViolationVertex : ruleViolationVertices) {
      Map<String, Object> ruleViolation = UtilClass.getMapFromNode(ruleViolationVertex);
      ruleViolations.put((String) ruleViolation.get(CommonConstants.CODE_PROPERTY),ruleViolation);
    }
    return ruleViolations;
  }

  public static Map<String, Object> getNormalizations(Vertex dataRule) throws Exception
  {
    Map<String, Object> normalizations = new HashMap<>();
    Iterable<Vertex> normalizationVertices = dataRule.getVertices(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_LINK);
    for (Vertex normalization : normalizationVertices) {
      Map<String, Object> normalizationMap = UtilClass.getMapFromNode(normalization);
      String type = normalization.getProperty(CommonConstants.TYPE_PROPERTY);
      if (type.equals(CommonConstants.TAG_PROPERTY)) {
        List<Map<String, Object>> tagValues = new ArrayList<>();
        Iterable<Vertex> tags = normalization.getVertices(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        Vertex tag = tags.iterator().next();
        String tagType = tag.getProperty(CommonConstants.TAG_TYPE_PROPERTY);
        Iterable<Edge> tagValueEdges = normalization.getEdges(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
        for (Edge tagValueEdge : tagValueEdges) {
          Map<String, Object> tagValue = new HashMap<>();
          int to = tagValueEdge.getProperty(CommonConstants.TO_PROPERTY);
          int from = tagValueEdge.getProperty(CommonConstants.FROM_PROPERTY);
          String innerTagId = tagValueEdge.getProperty(IDataRuleTagValues.INNER_TAG_ID);
          String id = tagValueEdge.getProperty(CommonConstants.ENTITY_ID_PROPERTY);
          tagValue.put(CommonConstants.TO_PROPERTY, to);
          tagValue.put(CommonConstants.FROM_PROPERTY, from);
          tagValue.put(CommonConstants.ID_PROPERTY, id);
          tagValue.put(IDataRuleTagValues.INNER_TAG_ID, innerTagId);
          // tagValue.put(CommonConstants.CODE_PROPERTY, UtilClass.getCode(tagValueEdge.getVertex(Direction.IN)));
          tagValues.add(tagValue);
          if (to == 0 && from == 0) {
            if (tagType.equals(CommonConstants.YES_NEUTRAL_TAG_TYPE_ID)) {
              tagValues.remove(tagValue);
            }
          }
        }
        normalizationMap.put(CommonConstants.TAG_VALUES, tagValues);
      }
      else if (type.equals(CommonConstants.ATTRIBUTE)) {
        String transformationType = normalization.getProperty(INormalization.TRANSFORMATION_TYPE);
        if (transformationType.equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
          fillAttributeValueIdForNormalization(normalization, normalizationMap);
        }
        else if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
          fillConcatenatedListForNormalization(normalization, normalizationMap);
        }
      }
      else if (type.equals(CommonConstants.ROLE_PROPERTY)) {
        List<String> values = new ArrayList<>();
        Iterable<Vertex> users = normalization.getVertices(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_USER_LINK);
        for (Vertex user : users) {
          String userId = user.getProperty(CommonConstants.CODE_PROPERTY);
          values.add(userId);
        }
        normalizationMap.put(CommonConstants.VALUES_PROPERTY, values);
      }
      else if (type.equals(CommonConstants.TYPE) || type.equals(CommonConstants.TAXONOMY)) {
        List<String> values = new ArrayList<>();
        Iterable<Vertex> entityVertices = normalization.getVertices(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        for (Vertex entity : entityVertices) {
          values.add(UtilClass.getCodeNew(entity));
        }
        normalizationMap.put(CommonConstants.VALUES_PROPERTY, values);
      }
      String key = (String) normalizationMap.get(CommonConstants.ENTITY_ID_PROPERTY);
      key = key == null? (String) normalizationMap.get(CommonConstants.TYPE) : key;
      normalizations.put(key, normalizationMap);
    }
    return normalizations;
  }
  
  //For Migration Only
  public static void fillNormalizationDataForMigration(Vertex dataRule,
      List<Map<String, Object>> normalizations) throws Exception
  {
    Iterable<Vertex> normalizationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.NORMALIZATION_LINK);
    for (Vertex normalization : normalizationVertices) {
      Map<String, Object> normalizationMap = UtilClass.getMapFromNode(normalization);
      String type = normalization.getProperty(CommonConstants.TYPE_PROPERTY);
      if (type.equals(CommonConstants.TAG_PROPERTY)) {
        List<Map<String, Object>> tagValues = new ArrayList<>();
        Iterable<Vertex> tags = normalization.getVertices(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        Vertex tag = tags.iterator()
            .next();
        String tagType = tag.getProperty(CommonConstants.TAG_TYPE_PROPERTY);
        Iterable<Edge> tagValueEdges = normalization.getEdges(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
        for (Edge tagValueEdge : tagValueEdges) {
          Map<String, Object> tagValue = new HashMap<>();
          int to = tagValueEdge.getProperty(CommonConstants.TO_PROPERTY);
          int from = tagValueEdge.getProperty(CommonConstants.FROM_PROPERTY);
          String innerTagId = tagValueEdge.getProperty(IDataRuleTagValues.INNER_TAG_ID);
          String id = tagValueEdge.getProperty(CommonConstants.ENTITY_ID_PROPERTY);
          tagValue.put(CommonConstants.TO_PROPERTY, to);
          tagValue.put(CommonConstants.FROM_PROPERTY, from);
          tagValue.put(CommonConstants.ID_PROPERTY, id);
          tagValue.put(IDataRuleTagValues.INNER_TAG_ID, innerTagId);
         // tagValue.put(CommonConstants.CODE_PROPERTY, UtilClass.getCode(tagValueEdge.getVertex(Direction.IN)));
          tagValues.add(tagValue);
          if (to == 0 && from == 0) {
            if (tagType.equals(CommonConstants.YES_NEUTRAL_TAG_TYPE_ID)) {
              tagValues.remove(tagValue);
            }
          }
        }

        normalizationMap.put(CommonConstants.TAG_VALUES, tagValues);
      }
      else if (type.equals(CommonConstants.ATTRIBUTE)) {
        String transformationType = normalization.getProperty(INormalization.TRANSFORMATION_TYPE);
        if (transformationType.equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
          fillAttributeValueIdForNormalization(normalization, normalizationMap);
        }
        else if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
          fillConcatenatedListForNormalization(normalization, normalizationMap);
        }
      }
      else if (type.equals(CommonConstants.ROLE_PROPERTY)) {
        List<String> values = new ArrayList<>();
        Iterable<Vertex> users = normalization.getVertices(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_USER_LINK);
        for (Vertex user : users) {
          String userId = user.getProperty(CommonConstants.CODE_PROPERTY);
          values.add(userId);
        }
        normalizationMap.put(CommonConstants.VALUES_PROPERTY, values);
      }
      else if (type.equals("type") || type.equals("taxonomy")) {
        List<String> values = new ArrayList<>();
        Iterable<Vertex> entityVertices = normalization.getVertices(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        for (Vertex entity : entityVertices) {
          values.add(UtilClass.getCodeNew(entity));
        }
        if(values.isEmpty()) {
          continue;
        }
        normalizationMap.put(CommonConstants.VALUES_PROPERTY, values);
      }
      normalizations.add(normalizationMap);
    }
  }
}
