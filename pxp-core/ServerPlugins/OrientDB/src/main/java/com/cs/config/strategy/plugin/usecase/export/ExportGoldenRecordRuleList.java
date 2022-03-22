package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportGoldenRecordRuleList extends AbstractOrientPlugin {
  
  public ExportGoldenRecordRuleList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<>();
    
    List<String> ruleCodes = (List<String>) requestMap.get("itemCodes");
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(ruleCodes, IGoldenRecordRule.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.GOLDEN_RECORD_RULE + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex ruleNode : resultIterable) {
      list.add(getGoldenRecordRuleFromNode(ruleNode));
    }
    
    responseMap.put("list", list);
    return responseMap;
  }
  
  private Map<String, Object> getGoldenRecordRuleFromNode(Vertex goldenRecordNode) throws Exception
  {
    Map<String, Object> ruleMap = UtilClass.getMapFromNode(goldenRecordNode);
    fillAttributesData(goldenRecordNode, ruleMap);
    fillTagsData(goldenRecordNode, ruleMap);
    fillKlassesData(goldenRecordNode, ruleMap);
    fillTaxonomiesData(goldenRecordNode, ruleMap);
    fillOrganizationsData(goldenRecordNode, ruleMap);
    fillMergeEffectData(goldenRecordNode, ruleMap);
    return ruleMap;
  }
  
  private void fillAttributesData(Vertex goldenRecordNode, Map<String, Object> ruleMap)
  {
    List<String> attributes = new ArrayList<>();
    Iterable<Vertex> attributesIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK);
    for (Vertex attribute : attributesIterator) {
      String attributeId = UtilClass.getCodeNew(attribute);
      attributes.add(attributeId);
    }
    ruleMap.put(IGoldenRecordRule.ATTRIBUTES, attributes);
  }
  
  private void fillTagsData(Vertex goldenRecordNode, Map<String, Object> ruleMap)
  {
    List<String> tags = new ArrayList<>();
    Iterable<Vertex> tagsIterator = goldenRecordNode.getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK);
    for (Vertex tag : tagsIterator) {
      String tagId = UtilClass.getCodeNew(tag);
      tags.add(tagId);
    }
    ruleMap.put(IGoldenRecordRule.TAGS, tags);
  }
  
  private void fillKlassesData(Vertex goldenRecordNode, Map<String, Object> ruleMap)
  {
    List<String> klasses = new ArrayList<>();
    Iterable<Vertex> klassIterator = goldenRecordNode.getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK);
    for (Vertex klass : klassIterator) {
      String klassId = UtilClass.getCodeNew(klass);
      klasses.add(klassId);
    }
    ruleMap.put(IGoldenRecordRule.KLASS_IDS, klasses);
  }
  
  private void fillTaxonomiesData(Vertex goldenRecordNode, Map<String, Object> ruleMap) throws Exception
  {
    List<String> taxonomyIds = new ArrayList<>();
    Iterable<Vertex> taxonomyIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK);
    for (Vertex taxonomy : taxonomyIterator) {
      String taxonomyId = UtilClass.getCodeNew(taxonomy);
      taxonomyIds.add(taxonomyId);
    }
    ruleMap.put(IGoldenRecordRule.TAXONOMY_IDS, taxonomyIds);
  }
  
  private void fillOrganizationsData(Vertex goldenRecordNode, Map<String, Object> ruleMap)
  {
    List<String> organizationIds = new ArrayList<>();
    Iterable<Vertex> organizationIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK);
    for (Vertex organization : organizationIterator) {
      String organizationId = UtilClass.getCodeNew(organization);
      organizationIds.add(organizationId);
    }
    ruleMap.put(IGoldenRecordRule.ORGANIZATIONS, organizationIds);
  }
  
  public static void fillMergeEffectData(Vertex goldenRecordNode, Map<String, Object> ruleMap)
  {
    Iterator<Vertex> mergeEffectIterator = (Iterator<Vertex>) goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK);
    if (!mergeEffectIterator.hasNext()) {
      return;
    }
    Vertex mergeEffect = mergeEffectIterator.next();
    Iterable<Vertex> effectTypesIterator = mergeEffect.getVertices(Direction.OUT, RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK);
    List<Map<String, Object>> effectAttributes = new ArrayList<>();
    List<Map<String, Object>> effectTags = new ArrayList<>();
    List<Map<String, Object>> effectRelationships = new ArrayList<>();
    List<Map<String, Object>> effectNatureRelationships = new ArrayList<>();
    for (Vertex effectType : effectTypesIterator) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(new ArrayList<>(), effectType);
      String entityType = (String) entityMap.get(IMergeEffectType.ENTITY_TYPE);
      if (entityType.equals(CommonConstants.ATTRIBUTES)) {
        effectAttributes.add(entityMap);
      }
      else if (entityType.equals(CommonConstants.TAGS)) {
        effectTags.add(entityMap);
      }
      else if (entityType.equals(CommonConstants.RELATIONSHIPS)) {
        effectRelationships.add(entityMap);
      }
      else if (entityType.equals(CommonConstants.NATURE_RELATIONSHIPS)) {
        effectNatureRelationships.add(entityMap);
      }
      String type = (String) entityMap.get(IMergeEffectType.TYPE);
      if (type.equals(CommonConstants.SUPPLIER_PRIORITY)) {
        Object rid = effectType.getId();
        String query = "select expand(inV('" + RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK + "')) from (select expand(outE('"
            + RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK + "')) from " + rid + " order by sequence)";
        List<String> supplierIds = new ArrayList<>();
        entityMap.put(IMergeEffectType.SUPPLIER_IDS, supplierIds);
        Iterable<Vertex> organizations = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
        for (Vertex organization : organizations) {
          String organizationId = UtilClass.getCodeNew(organization);
          supplierIds.add(organizationId);
        }
      }
    }
    Map<String, Object> mergeEffectMap = new HashMap<>();
    mergeEffectMap.put(IMergeEffect.ATTRIBUTES, effectAttributes);
    mergeEffectMap.put(IMergeEffect.TAGS, effectTags);
    mergeEffectMap.put(IMergeEffect.RELATIONSHIPS, effectRelationships);
    mergeEffectMap.put(IMergeEffect.NATURE_RELATIONSHIPS, effectNatureRelationships);
    ruleMap.put(IGoldenRecordRule.MERGE_EFFECT, mergeEffectMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportGoldenRecordRuleList/*" };
  }
}
