package com.cs.config.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GoldenRecordRuleUtil {
  
  public static void handleAddedAttributes(Vertex goldenRecordRule, List<String> addedAttributes)
      throws Exception
  {
    for (String attributeId : addedAttributes) {
      Vertex attribute = UtilClass.getVertexByIndexedId(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      goldenRecordRule.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK,
          attribute);
    }
  }
  
  public static void handleDeletedAttributes(Vertex goldenRecordRule,
      List<String> deletedAttributes, List<String> addedAttributes) 
      throws Exception
  {
    Iterable<Edge> edges = goldenRecordRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK);
    for (Edge edge : edges) {
      Vertex attribute = edge.getVertex(Direction.IN);
      String attributeCode = UtilClass.getCodeNew(attribute);
      if (deletedAttributes.contains(attributeCode)) {
        edge.remove();
      }
      if (addedAttributes.contains(attributeCode)) {
        addedAttributes.remove(attributeCode);
      }
    }
  }
  
  public static void handleAddedTags(Vertex goldenRecordRule, List<String> addedTags)
      throws Exception
  {
    for (String tagId : addedTags) {
      Vertex tag = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
      goldenRecordRule.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK, tag);
    }
  }
  
  public static void handleDeletedTags(Vertex goldenRecordRule, List<String> deletedTags,
      List<String> addedTags)
      throws Exception
  {
    Iterable<Edge> edges = goldenRecordRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK);
    for (Edge edge : edges) {
      Vertex tag = edge.getVertex(Direction.IN);
      String tagCode = UtilClass.getCodeNew(tag);
      if (deletedTags.contains(tagCode)) {
        edge.remove();
      }
      if (addedTags.contains(tagCode)) {
        addedTags.remove(tagCode);
      }
    }
  }
  
  public static void handleAddedKlasses(Vertex goldenRecordRule, List<String> addedKlasses)
      throws Exception
  {
    
    for (String klassId : addedKlasses) {
      Vertex klass = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      goldenRecordRule.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK, klass);
    }
  }
  
  public static void handleDeletedKlasses(Vertex goldenRecordRule, List<String> deletedKlasses,
      List<String> addedKlasses)
      throws Exception
  {
    Iterable<Edge> edges = goldenRecordRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK);
    for (Edge edge : edges) {
      Vertex klass = edge.getVertex(Direction.IN);
      String klassCode = UtilClass.getCodeNew(klass);
      if (deletedKlasses.contains(klassCode)) {
        edge.remove();
      }
      if (addedKlasses.contains(klassCode)) {
        addedKlasses.remove(klassCode);
      }
    }
  }
  
  public static void handleAddedTaxonomies(Vertex goldenRecordRule, List<String> addedTaxonomies)
      throws Exception
  {
    for (String taxonomyId : addedTaxonomies) {
      Vertex taxonomy = UtilClass.getVertexByIndexedId(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      goldenRecordRule.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK,
          taxonomy);
    }
  }
  
  public static void handleDeletedTaxonomies(Vertex goldenRecordRule,
      List<String> deletedTaxonomies, List<String> addedTaxonomies) 
      throws Exception
  {
    Iterable<Edge> edges = goldenRecordRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK);
    for (Edge edge : edges) {
      Vertex taxonomy = edge.getVertex(Direction.IN);
      String taxonomyCode = UtilClass.getCodeNew(taxonomy);
      if (deletedTaxonomies.contains(taxonomyCode)) {
        edge.remove();
      }
      if (addedTaxonomies.contains(taxonomyCode)) {
        addedTaxonomies.remove(taxonomyCode);
      }
    }
  }
  
  public static void handleAddedOrganizations(Vertex goldenRecordRule,
      List<String> addedOrganizations) throws Exception
  {
    for (String taxonomyId : addedOrganizations) {
      Vertex taxonomy = UtilClass.getVertexByIndexedId(taxonomyId,
          VertexLabelConstants.ORGANIZATION);
      goldenRecordRule.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK,
          taxonomy);
    }
  }
  
  public static void handleDeletedOrganizations(Vertex goldenRecordRule,
      List<String> deletedOrganizations, List<String> addedOrganizations) 
      throws Exception
  {
    Iterable<Edge> edges = goldenRecordRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK);
    for (Edge edge : edges) {
      Vertex organization = edge.getVertex(Direction.IN);
      String organizationCode = UtilClass.getCodeNew(organization);
      if (deletedOrganizations.contains(organizationCode)) {
        edge.remove();
      }
      if (addedOrganizations.contains(organizationCode)) {
        addedOrganizations.remove(organizationCode);
      }
    }
  }
  
  public static void handleAddedEndpoints(Vertex goldenRecordRule, List<String> addedEndpoints)
      throws Exception
  {
    for (String endpointId : addedEndpoints) {
      Vertex endpoint = UtilClass.getVertexByIndexedId(endpointId, VertexLabelConstants.ENDPOINT);
      goldenRecordRule.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_ENDPOINT_LINK,
          endpoint);
    }
  }
  
  public static void handleDeletedEndpoints(Vertex goldenRecordRule, List<String> deletedEndpoints,
      List<String> addedEndpoints)
      throws Exception
  {
    Iterable<Edge> edges = goldenRecordRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ENDPOINT_LINK);
    for (Edge edge : edges) {
      Vertex endpoint = edge.getVertex(Direction.IN);
      String endpointCode = UtilClass.getCodeNew(endpoint);
      if (deletedEndpoints.contains(endpointCode)) {
        edge.remove();
      }
      if (addedEndpoints.contains(endpointCode)) {
        addedEndpoints.remove(endpointCode);
      }
    }
  }
  
  public static Map<String, Object> getGoldenRecordRuleFromNode(Vertex goldenRecordNode)
      throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> ruleMap = UtilClass.getMapFromNode(goldenRecordNode);
    returnMap.put(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE, ruleMap);
    fillAttributesData(goldenRecordNode, returnMap);
    fillTagsData(goldenRecordNode, returnMap);
    fillKlassesData(goldenRecordNode, returnMap);
    fillTaxonomiesData(goldenRecordNode, returnMap);
    fillOrganizationsData(goldenRecordNode, returnMap);
    fillEndpointsData(goldenRecordNode, returnMap);
    fillMergeEffectData(goldenRecordNode, returnMap);
    return returnMap;
  }
  
  public static void fillAttributesData(Vertex goldenRecordNode, Map<String, Object> returnMap)
  {
    List<String> attributes = new ArrayList<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    Iterable<Vertex> attributesIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK);
    for (Vertex attribute : attributesIterator) {
      String attributeId = UtilClass.getCodeNew(attribute);
      attributes.add(attributeId);
      String label = (String) UtilClass.getValueByLanguage(attribute, IIdLabelModel.LABEL);
      Map<String, Object> idLabelCodeMap = new HashMap<>();
      idLabelCodeMap.put(IIdLabelCodeModel.ID, attributeId);
      idLabelCodeMap.put(IIdLabelCodeModel.LABEL, label);
      idLabelCodeMap.put(IIdLabelCodeModel.CODE, (String) attribute.getProperty(IAttribute.CODE));
      referencedAttributes.put(attributeId, idLabelCodeMap);
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.ATTRIBUTES, attributes);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_ATTRIBUTES, referencedAttributes);
  }
  
  public static void fillTagsData(Vertex goldenRecordNode, Map<String, Object> returnMap)
  {
    List<String> tags = new ArrayList<>();
    Map<String, Object> referencedTags = new HashMap<>();
    Iterable<Vertex> tagsIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK);
    for (Vertex tag : tagsIterator) {
      String tagId = UtilClass.getCodeNew(tag);
      tags.add(tagId);
      String label = (String) UtilClass.getValueByLanguage(tag, IIdLabelModel.LABEL);
      Map<String, Object> idLabelCodeMap = new HashMap<>();
      idLabelCodeMap.put(IIdLabelCodeModel.ID, tagId);
      idLabelCodeMap.put(IIdLabelCodeModel.LABEL, label);
      idLabelCodeMap.put(IIdLabelCodeModel.CODE, (String) tag.getProperty(ITag.CODE));
      referencedTags.put(tagId, idLabelCodeMap);
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.TAGS, tags);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_TAGS, referencedTags);
  }
  
  public static void fillKlassesData(Vertex goldenRecordNode, Map<String, Object> returnMap)
  {
    List<String> klasses = new ArrayList<>();
    Map<String, Object> referencedKlasses = new HashMap<>();
    Iterable<Vertex> klassIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK);
    for (Vertex klass : klassIterator) {
      String klassId = UtilClass.getCodeNew(klass);
      klasses.add(klassId);
      String label = (String) UtilClass.getValueByLanguage(klass, IIdLabelModel.LABEL);
      Map<String, Object> idLabelNatureMap = new HashMap<>();
      idLabelNatureMap.put(IIdLabelNatureTypeModel.ID, klassId);
      idLabelNatureMap.put(IIdLabelNatureTypeModel.LABEL, label);
      idLabelNatureMap.put(IIdLabelNatureTypeModel.IS_NATURE, klass.getProperty(IKlass.IS_NATURE));
      idLabelNatureMap.put(IIdLabelNatureTypeModel.CODE, klass.getProperty(IKlass.CODE));
      idLabelNatureMap.put(IIdLabelNatureTypeModel.TYPE, klass.getProperty(IKlass.TYPE));
      idLabelNatureMap.put(IIdLabelNatureTypeModel.IID, klass.getProperty(IKlass.CLASSIFIER_IID));
      UtilClass.fetchIconInfo(klass, idLabelNatureMap);
      referencedKlasses.put(klassId, idLabelNatureMap);
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.KLASS_IDS, klasses);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_KLASSES, referencedKlasses);
  }
  
  public static void fillTaxonomiesData(Vertex goldenRecordNode, Map<String, Object> returnMap)
      throws Exception
  {
    List<String> taxonomyIds = new ArrayList<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Iterable<Vertex> taxonomyIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK);
    for (Vertex taxonomy : taxonomyIterator) {
      String taxonomyId = UtilClass.getCodeNew(taxonomy);
      taxonomyIds.add(taxonomyId);
      Map<String, Object> taxonomyMap = new HashMap<>();
      TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomy);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.TAXONOMY_IDS, taxonomyIds);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
  }
  
  public static void fillOrganizationsData(Vertex goldenRecordNode, Map<String, Object> returnMap)
  {
    List<String> organizationIds = new ArrayList<>();
    Map<String, Object> referencedOrganizations = new HashMap<>();
    Iterable<Vertex> organizationIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK);
    for (Vertex organization : organizationIterator) {
      String organizationId = UtilClass.getCodeNew(organization);
      organizationIds.add(organizationId);
      String label = (String) UtilClass.getValueByLanguage(organization, IIdLabelModel.LABEL);
      Map<String, Object> idLabelCodeMap = new HashMap<>();
      idLabelCodeMap.put(IIdLabelCodeModel.ID, organizationId);
      idLabelCodeMap.put(IIdLabelCodeModel.LABEL, label);
      idLabelCodeMap.put(IIdLabelCodeModel.CODE,
          (String) organization.getProperty(CommonConstants.CODE_PROPERTY));
      referencedOrganizations.put(organizationId, idLabelCodeMap);
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.ORGANIZATIONS, organizationIds);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_ORGANIZATIONS, referencedOrganizations);
  }
  
  public static void fillEndpointsData(Vertex goldenRecordNode, Map<String, Object> returnMap)
  {
    List<String> endpointIds = new ArrayList<>();
    Map<String, Object> referencedEndpoints = new HashMap<>();
    Iterable<Vertex> endpointIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ENDPOINT_LINK);
    for (Vertex endpoint : endpointIterator) {
      String endpointId = UtilClass.getCodeNew(endpoint);
      endpointIds.add(endpointId);
      String label = (String) UtilClass.getValueByLanguage(endpoint, IIdLabelModel.LABEL);
      Map<String, Object> idLabelCodeMap = new HashMap<>();
      idLabelCodeMap.put(IIdLabelCodeModel.ID, endpointId);
      idLabelCodeMap.put(IIdLabelCodeModel.LABEL, label);
      idLabelCodeMap.put(IIdLabelCodeModel.CODE, (String) endpoint.getProperty(IEndpoint.CODE));
      referencedEndpoints.put(endpointId, idLabelCodeMap);
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.ENDPOINTS, endpointIds);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_ENDPOINTS, referencedEndpoints);
  }
  
  public static void handleAddedEffectEntities(Vertex mergeEffect,
      List<Map<String, Object>> entities, String vertexLabel, List<String> existingEntityIds)
      throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.EFFECT_TYPE,
        CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> entityMap : entities) {
      String entityId = (String) entityMap.get(IMergeEffectType.ENTITY_ID);
      if (existingEntityIds.contains(entityId)) {
        continue;
      }
      Vertex entityVertex = UtilClass.getVertexByIndexedId(entityId, vertexLabel);
      Vertex effectType = UtilClass.createNode(entityMap, vertexType,
          Arrays.asList(IMergeEffectType.SUPPLIER_IDS));
      mergeEffect.addEdge(RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK, effectType);
      effectType.addEdge(RelationshipLabelConstants.EFFECT_TYPE_ENTITY_LINK, entityVertex);
      linkSuppliersToEffect(effectType,
          (List<String>) entityMap.get(IMergeEffectType.SUPPLIER_IDS));
    }
  }
  
  public static void linkSuppliersToEffect(Vertex effectType, List<String> supplierIds)
      throws Exception
  {
    for (int index = 1; index <= supplierIds.size(); index++) {
      Vertex supplierVertex = UtilClass.getVertexByIndexedId(supplierIds.get(index - 1),
          VertexLabelConstants.ORGANIZATION);
      Edge edge = effectType.addEdge(RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK,
          supplierVertex);
      edge.setProperty("sequence", index);
    }
  }
  
  public static void handleDeletedEffectEntities(Vertex mergeEffect, List<String> entityIdsToDelete,
      List<String> existingEntityIds, String entityType)
      throws Exception
  {
    Iterable<Edge> effectTypeLinks = mergeEffect.getEdges(Direction.OUT,
        RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK);
    for (Edge edge : effectTypeLinks) {
      Vertex typeVertex = edge.getVertex(Direction.IN);
      if (entityType != null && !entityType.equals(typeVertex.getProperty(CommonConstants.ENTITYTYPE))) {
        continue;
      }
      existingEntityIds.add(typeVertex.getProperty(IMergeEffectType.ENTITY_ID));
      if (entityIdsToDelete.contains(typeVertex.getProperty(IMergeEffectType.ENTITY_ID))) {
        edge.remove();
        String type = typeVertex.getProperty(IMergeEffectType.TYPE);
        if (type.equals(CommonConstants.SUPPLIER_PRIORITY)) {
          unlinkSuppliersFromEffect(typeVertex);
        }
      }
    }
  }
  
  public static void unlinkSuppliersFromEffect(Vertex effectType) throws Exception
  {
    Iterable<Edge> effectSupplierLinksIterable = effectType.getEdges(Direction.OUT,
        RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK);
    for (Edge edge : effectSupplierLinksIterable) {
      edge.remove();
    }
  }
  
  public static void fillMergeEffectData(Vertex goldenRecordNode, Map<String, Object> returnMap)
  {
    Iterator<Vertex> mergeEffectIterator = (Iterator<Vertex>) goldenRecordNode.getVertices(
        Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK);
    if (!mergeEffectIterator.hasNext()) {
      return;
    }
    Vertex mergeEffect = mergeEffectIterator.next();
    Iterable<Vertex> effectTypesIterator = mergeEffect.getVertices(Direction.OUT,
        RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK);
    List<Map<String, Object>> effectAttributes = new ArrayList<>();
    List<Map<String, Object>> effectTags = new ArrayList<>();
    List<Map<String, Object>> effectRelationships = new ArrayList<>();
    List<Map<String, Object>> effectNatureRelationships = new ArrayList<>();
    Map<String, Object> referencedAttributes = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.REFERENCED_TAGS);
    Map<String, Object> referencedOrganizations = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.REFERENCED_ORGANIZATIONS);
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_RELATIONSHIPS, referencedRelationships);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    for (Vertex effectType : effectTypesIterator) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(new ArrayList<>(), effectType);
      Iterator<Vertex> entityIterator = effectType
          .getVertices(Direction.OUT, RelationshipLabelConstants.EFFECT_TYPE_ENTITY_LINK)
          .iterator();
      
      Vertex entityVertex = entityIterator.next();
      String entityId = UtilClass.getCodeNew(entityVertex);
      String label = (String) UtilClass.getValueByLanguage(entityVertex, IIdLabelModel.LABEL);
      Map<String, Object> idLabelCodeMap = new HashMap<>();
      idLabelCodeMap.put(IIdLabelCodeModel.ID, entityId);
      idLabelCodeMap.put(IIdLabelCodeModel.LABEL, label);
      idLabelCodeMap.put(IIdLabelCodeModel.CODE,
          (String) entityVertex.getProperty(CommonConstants.CODE_PROPERTY));
      String entityType = (String) entityMap.get(IMergeEffectType.ENTITY_TYPE);
      if (entityType.equals(CommonConstants.ATTRIBUTES)) {
        effectAttributes.add(entityMap);
        referencedAttributes.put(entityId, idLabelCodeMap);
      }
      else if (entityType.equals(CommonConstants.TAGS)) {
        effectTags.add(entityMap);
        referencedTags.put(entityId, idLabelCodeMap);
      }
      else if (entityType.equals(CommonConstants.RELATIONSHIPS)) {
        effectRelationships.add(entityMap);
        referencedRelationships.put(entityId, idLabelCodeMap);
      }
      else if (entityType.equals(CommonConstants.NATURE_RELATIONSHIPS)) {
        effectNatureRelationships.add(entityMap);
        referencedNatureRelationships.put(entityId, idLabelCodeMap);
      }
      String type = (String) entityMap.get(IMergeEffectType.TYPE);
      if (type.equals(CommonConstants.SUPPLIER_PRIORITY)) {
        Object rid = effectType.getId();
        String query = "select expand(inV('"
            + RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK
            + "')) from (select expand(outE('"
            + RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK + "')) from " + rid
            + " order by sequence)";
        List<String> supplierIds = new ArrayList<>();
        entityMap.put(IMergeEffectType.SUPPLIER_IDS, supplierIds);
        Iterable<Vertex> organizations = UtilClass.getGraph()
            .command(new OCommandSQL(query))
            .execute();
        for (Vertex organization : organizations) {
          String organizationId = UtilClass.getCodeNew(organization);
          supplierIds.add(organizationId);
          String organizationLabel = (String) UtilClass.getValueByLanguage(organization,
              IIdLabelModel.LABEL);
          Map<String, Object> idLabelCode = new HashMap<>();
          idLabelCode.put(IIdLabelCodeModel.ID, organizationId);
          idLabelCode.put(IIdLabelCodeModel.LABEL, organizationLabel);
          idLabelCode.put(IIdLabelCodeModel.CODE,
              (String) organization.getProperty(CommonConstants.CODE_PROPERTY));
          ;
          referencedOrganizations.put(organizationId, idLabelCode);
        }
      }
    }
    Map<String, Object> mergeEffectMap = new HashMap<>();
    mergeEffectMap.put(IMergeEffect.ATTRIBUTES, effectAttributes);
    mergeEffectMap.put(IMergeEffect.TAGS, effectTags);
    mergeEffectMap.put(IMergeEffect.RELATIONSHIPS, effectRelationships);
    mergeEffectMap.put(IMergeEffect.NATURE_RELATIONSHIPS, effectNatureRelationships);
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.MERGE_EFFECT, mergeEffectMap);
  }
  
  public static void deleteGoldenRecordMergeEffectTypeNode(Vertex node)
  {
    Iterable<Vertex> vertices = node.getVertices(Direction.IN,
        RelationshipLabelConstants.EFFECT_TYPE_ENTITY_LINK);
    vertices.forEach(vertex -> {
      vertex.remove();
    });
  }
  
  public static void manageModifiedEffectEntity(Vertex mergeEffect,
      List<Map<String, Object>> modifiedEffectEntities) throws Exception
  {
    Object rid = mergeEffect.getId();
    for (Map<String, Object> modifiedEffectEntity : modifiedEffectEntities) {
      String entityId = (String) modifiedEffectEntity.get(IMergeEffectType.ENTITY_ID);
      String query = "select from (select expand (out('"
          + RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK + "')) from " + rid + ") where "
          + IMergeEffectType.ENTITY_ID + " IN " + EntityUtil.quoteIt(entityId);
      Iterable<Vertex> effectTypesIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      Iterator<Vertex> effectTypes = effectTypesIterable.iterator();
      if (effectTypes.hasNext()) {
        Vertex effectType = effectTypes.next();
        String type = (String) modifiedEffectEntity.get(IMergeEffectType.TYPE);
        effectType.setProperty(IMergeEffectType.TYPE, type);
        GoldenRecordRuleUtil.unlinkSuppliersFromEffect(effectType);
        GoldenRecordRuleUtil.linkSuppliersToEffect(effectType,
            (List<String>) modifiedEffectEntity.get(IMergeEffectType.SUPPLIER_IDS));
      }
    }
  }
}
