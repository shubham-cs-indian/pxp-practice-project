package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.goldenrecord.GoldenRecordRuleUtil;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForEvaluateGoldenRecordModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.IteratorUtils;

public class GetGoldenRecordRulesAssociatedWithInstance extends AbstractOrientPlugin {
  
  public GetGoldenRecordRulesAssociatedWithInstance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGoldenRecordRulesAssociatedWithInstance/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(IKlassInstanceTypeModel.TYPES);
    List<String> taxonomyIds = (List<String>) requestMap.get(IKlassInstanceTypeModel.TAXONOMY_IDS);
    String organisationId = (String) requestMap.get(IKlassInstanceTypeModel.ORAGANIZATION_ID);
    String endpointId = (String) requestMap.get(IKlassInstanceTypeModel.ENDPOINT_ID);
    String physicalCatalogId = (String) requestMap.get(IKlassInstanceTypeModel.PHYSICAL_CATALOG_ID);
    
    List<String> klassTaxonomyRIds = new ArrayList<String>();
    List<String> nonNatureKlassCids = new ArrayList<>();
    Vertex natureKlassNode = fillNonNatureKlassIdsAndGetNatureKlassNode(klassIds,
        nonNatureKlassCids, klassTaxonomyRIds);
    List<String> taxonomyCids = new ArrayList<>();
    fillTaxonomyCids(taxonomyIds, taxonomyCids, klassTaxonomyRIds);
    
    Set<Vertex> allApplicableGoldenRecordRules = new HashSet<>();
    
    Object natureKlassRid = natureKlassNode.getId();
    List<Object> natureKlassRids = new ArrayList<>();
    natureKlassRids.add(natureKlassRid);
    String query = getGoldentRecordRuleQuery(natureKlassRids, organisationId, physicalCatalogId,
        endpointId, RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK);
    Iterable<Vertex> goldenRecordRuleVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex goldenRecordRule : goldenRecordRuleVertices) {
      Iterator<Vertex> linkedKlasses = goldenRecordRule
          .getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK)
          .iterator();
      List<Vertex> linkedKlassesList = IteratorUtils.toList(linkedKlasses);
      linkedKlassesList.remove(natureKlassNode);
      
      Iterator<Vertex> linkedTaxonomies = goldenRecordRule
          .getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK)
          .iterator();
      List<Vertex> linkedTaxonomyList = IteratorUtils.toList(linkedTaxonomies);
      
      // rule only has nature klass linked to it
      if (linkedKlassesList.isEmpty() && linkedTaxonomyList.isEmpty()) {
        allApplicableGoldenRecordRules.add(goldenRecordRule);
        continue;
      }
      
      Boolean isApplicableForKlass = true;
      List<String> linkedKlassesCids = UtilClass.getCodes(linkedKlassesList);
      linkedKlassesCids.retainAll(nonNatureKlassCids);
      if (!linkedKlassesList.isEmpty() && linkedKlassesCids.isEmpty()) {
        isApplicableForKlass = false;
      }
      
      Boolean isApplicableForTaxonomy = true;
      List<String> linkedTaxonomyCids = UtilClass.getCodes(linkedTaxonomyList);
      linkedTaxonomyCids.retainAll(taxonomyCids);
      if (!linkedTaxonomyList.isEmpty() && linkedTaxonomyCids.isEmpty()) {
        isApplicableForTaxonomy = false;
      }
      
      if (isApplicableForKlass && isApplicableForTaxonomy) {
        allApplicableGoldenRecordRules.add(goldenRecordRule);
      }
    }
    
    Map<String, Object> response = getResponnseMap();
    for (Vertex applicableGoldenRecordRule : allApplicableGoldenRecordRules) {
      fillGoldenRecordList(response, applicableGoldenRecordRule);
    }
    
    response.put(IConfigDetailsForEvaluateGoldenRecordModel.BOOLEAN_TAGS_TO_PRESERVE,
        getBooleanTagsLinkedWithArticle(klassTaxonomyRIds));
    
    return response;
  }
  
  private Map<String, Object> getResponnseMap()
  {
    Map<String, Object> referencedTags = new HashMap<>();
    Set<Map<String, Object>> goldenRecordRulesSet = new HashSet<Map<String, Object>>();
    Map<String, Object> response = new HashMap<>();
    response.put(IConfigDetailsForEvaluateGoldenRecordModel.GOLDEN_RECORD_RULES,
        goldenRecordRulesSet);
    response.put(IConfigDetailsForEvaluateGoldenRecordModel.REFERENCED_TAGS, referencedTags);
    return response;
  }
  
  /* @SuppressWarnings("unchecked")
  private void fillGoldenRecordList(Set<Map<String, Object>> goldenRecordRulesSet, Vertex goldenRecordNode) throws Exception
    {
      Map<String, Object> goldenRecordRuleMap = GoldenRecordRuleUtil.getGoldenRecordRuleFromNode(goldenRecordNode);
      goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ATTRIBUTES);
      goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_TAGS);
      goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_KLASSES);
      goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_TAXONOMIES);
      goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ORGANIZATIONS);
      goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ENDPOINTS);
      goldenRecordRulesSet.add((Map<String, Object>)goldenRecordRuleMap.get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE));
    }*/
  
  private void fillGoldenRecordList(Map<String, Object> response, Vertex goldenRecordNode)
      throws Exception
  {
    Map<String, Object> referencedTags = (Map<String, Object>) response
        .get(IConfigDetailsForEvaluateGoldenRecordModel.REFERENCED_TAGS);
    Set<Map<String, Object>> goldenRecordRulesSet = (Set<Map<String, Object>>) response
        .get(IConfigDetailsForEvaluateGoldenRecordModel.GOLDEN_RECORD_RULES);
    
    Map<String, Object> goldenRecordRuleMap = getGoldenRecordRuleFromNode(goldenRecordNode,
        referencedTags);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ATTRIBUTES);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_TAGS);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_KLASSES);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_TAXONOMIES);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ORGANIZATIONS);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ENDPOINTS);
    goldenRecordRulesSet.add((Map<String, Object>) goldenRecordRuleMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE));
  }
  
  public static Map<String, Object> getGoldenRecordRuleFromNode(Vertex goldenRecordNode,
      Map<String, Object> referencedTags) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> ruleMap = UtilClass.getMapFromNode(goldenRecordNode);
    returnMap.put(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE, ruleMap);
    GoldenRecordRuleUtil.fillAttributesData(goldenRecordNode, returnMap);
    fillTagsData(goldenRecordNode, returnMap, referencedTags);
    GoldenRecordRuleUtil.fillKlassesData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillTaxonomiesData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillOrganizationsData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillEndpointsData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillMergeEffectData(goldenRecordNode, returnMap);
    return returnMap;
  }
  
  public static void fillTagsData(Vertex goldenRecordNode, Map<String, Object> returnMap,
      Map<String, Object> referencedTags) throws Exception
  {
    List<String> tags = new ArrayList<>();
    Iterable<Vertex> tagsIterator = goldenRecordNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK);
    for (Vertex tag : tagsIterator) {
      String tagId = UtilClass.getCodeNew(tag);
      tags.add(tagId);
      if (!referencedTags.containsKey(tagId)) {
        Map<String, Object> tagMap = TagUtils.getTagMap(tag, true);
        referencedTags.put(tagId, tagMap);
      }
    }
    Map<String, Object> ruleMap = (Map<String, Object>) returnMap
        .get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE);
    ruleMap.put(IGoldenRecordRule.TAGS, tags);
    returnMap.put(IGetGoldenRecordRuleModel.REFERENCED_TAGS, new HashMap<>());
  }
  
  protected String getGoldentRecordRuleQuery(List<Object> rids, String organisationId,
      String physicalCatalogId, String endpointId, String relationshipLabel)
  {
    String query = "SELECT FROM (SELECT EXPAND(IN('" + relationshipLabel + "')) FROM " + rids
        + " )";
    
    // get kpi if
    // 1. goldenRecordRule is connected with the provided organization
    // 2. goldenRecordRule is not link with any organization(i.e it is link with
    // all the
    // organization)
    query = query + " WHERE (( OUT('"
        + RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK + "').code CONTAINS '"
        + organisationId + "' ) OR OUT('"
        + RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK + "').size() = 0 ) AND ";
    
    // get kpi if
    // 1. physicalCatalogIds contains physicalCatalogId
    // 2. physicalCatalogIdy is empty(i.e applicable for all physicalCatalogs)
    query = query + " ( " + IGoldenRecordRuleModel.PHYSICAL_CATALOG_IDS + " CONTAINS '"
        + physicalCatalogId + "' OR " + IGoldenRecordRuleModel.PHYSICAL_CATALOG_IDS
        + ".size() = 0 ) ";
    
    if (physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      // get goldenRecordRule if
      // 1. goldenRecordRule is connected with the provided endpoint
      // 2. goldenRecordRule is not link with any endpoint(i.e it is link with
      // all the endpoint)
      query = query + " AND (( OUT('" + RelationshipLabelConstants.GOLDEN_RECORD_RULE_ENDPOINT_LINK
          + "').code CONTAINS '" + endpointId + "' ) OR OUT('"
          + RelationshipLabelConstants.GOLDEN_RECORD_RULE_ENDPOINT_LINK + "').size() = 0)";
    }
    
    return query;
  }
  
  protected Vertex fillNonNatureKlassIdsAndGetNatureKlassNode(List<String> klassIds,
      List<String> nonNatureKlassCids, List<String> klassTaxonomyRIds) 
          throws Exception
  {
    Vertex natureKlassNode = null;
    Iterable<Vertex> klassNodes = UtilClass.getVerticesByIndexedIds(klassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    for (Vertex klass : klassNodes) {
      klassTaxonomyRIds.add(klass.getId().toString());
      Boolean isNature = klass.getProperty(IKlass.IS_NATURE);
      if (isNature != null && isNature) {
        natureKlassNode = klass;
        continue;
      }
      nonNatureKlassCids.add(UtilClass.getCodeNew(klass));
    }
    return natureKlassNode;
  }
  
  protected void fillTaxonomyCids(List<String> taxonomyIds, List<String> taxonomyCids,
      List<String> klassTaxonomyRIds)
          throws Exception
  {
    Iterable<Vertex> taxonomyNodes = UtilClass.getVerticesByIndexedIds(taxonomyIds,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    for (Vertex taxonomy : taxonomyNodes) {
      klassTaxonomyRIds.add(taxonomy.getId().toString());
      taxonomyCids.add(UtilClass.getCodeNew(taxonomy));
    }
  }
  
  /**
   * This method required to filter out boolean tags with referenced tags for
   * evaluating the golden record bucket instance
   * 
   * @param klassTaxonomyRIds - klass & taxonomy vertex ids
   * @return boolean tags codes
   */
  private List<String> getBooleanTagsLinkedWithArticle(List<String> klassTaxonomyRIds)
  {
    List<String> booleanTagsToPreserve = new ArrayList<String>();
    String queryForFetchingBooleanTags = "select expand(out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[" + CommonConstants.TAG_TYPE_PROPERTY + "='"
        + CommonConstants.BOOLEAN_TAG_TYPE_ID + "']) from(select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')[" + CommonConstants.TYPE_PROPERTY
        + "='tag']) from  [" + String.join(",", klassTaxonomyRIds) + "])";
    
    Iterable<Vertex> booleanTagVertices = UtilClass.getGraph()
        .command(new OCommandSQL(queryForFetchingBooleanTags))
        .execute();
    
    for (Vertex booleanTag : booleanTagVertices)
    {
      booleanTagsToPreserve.add(UtilClass.getCodeNew(booleanTag));
    }
    
    return booleanTagsToPreserve;
  }
  
}
