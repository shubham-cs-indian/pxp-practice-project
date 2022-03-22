package com.cs.config.strategy.plugin.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class SyncConfigToRDBMS extends AbstractOrientPlugin {
  
  private static final String CODE                    = "code";
  private static final String TYPE                    = "type";
  private static final String CHILDRENS               = "childrens";
  private static final String USERNAME                = "userName";
  private static final String LIST                    = "list";
  private static final String COUNT                   = "count";
  private static final String VERTEX_TYPE             = "vertexType";
  private static final String FROM                    = "from";
  private static final String SIZE                    = "size";
  private static final String DATA_RULE_RESPONSE_LIST = "dataRuleResponseList";
  
  public SyncConfigToRDBMS(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SyncConfigToRDBMS/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String vertexType = (String) requestMap.get(VERTEX_TYPE);
    int from = (int) requestMap.get(FROM);
    int size = (int) requestMap.get(SIZE);
    Map<String, Object> returnMap = new HashMap<>();
    
    switch (vertexType) {
      case VertexLabelConstants.ENTITY_TAG:
        fillTags(from, size, returnMap);
        break;
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
        fillKlassInfo(from, size, returnMap);
        break;
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
      case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
      case VertexLabelConstants.NATURE_RELATIONSHIP:
      case VertexLabelConstants.VARIANT_CONTEXT:
      case VertexLabelConstants.ENTITY_TYPE_TASK:
        fillInfo(from, size, returnMap, vertexType);
        break;
      case VertexLabelConstants.ENTITY_TYPE_USER:
        fillUsers(from, size, returnMap);
        break;
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
        fillTaxonomy(from, size, returnMap);
        break;
      case VertexLabelConstants.LANGUAGE:
        fillLanguage(from, size, returnMap);
        break;
      case VertexLabelConstants.ATTRIBUTION_TAXONOMY:
        fillAttributionTaxonomy(from, size, returnMap);
        break;
      case VertexLabelConstants.DATA_RULE:
        fillDataRules(from, size, returnMap);
        break;
      default:
        break;
    }
    
    return returnMap;
  }
  
  private void fillDataRules(int from, int size, Map<String, Object> returnMap) throws Exception
  {
    long count;
    List<Map<String, Object>> list = new ArrayList<>();
    String countQuery = "select count(*) from " + VertexLabelConstants.DATA_RULE;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put(COUNT, count);

    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.DATA_RULE + " skip  " + from + " limit " + size)).execute();
    
    for (Vertex dataRuleNode : searchResults) {
      
      Map<String, Object> dataRuleMap = getDataRuleFromNode(dataRuleNode, false);
      Boolean isLanguageDependent = (Boolean) dataRuleMap.get(IDataRule.IS_LANGUAGE_DEPENDENT);
      if (isLanguageDependent) {
        List<String> languages = (List<String>) dataRuleMap.get(IDataRule.LANGUAGES);
        if (languages.isEmpty()) {
          Iterable<Vertex> verticesOfClass = UtilClass.getGraph().getVerticesOfClass(VertexLabelConstants.LANGUAGE);
          
          for (Vertex language : verticesOfClass) {
            languages.add(language.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
      }
      dataRuleMap.put(IDataRuleModel.IS_PHYSICAL_CATALOGS_CHANGED, false);
      GetDataRuleUtils.fillConfigDetails(dataRuleMap);
      UtilClass.getGraph().commit();
      
      List<String> klassIds = new ArrayList<>();
      Vertex ruleNode = UtilClass.getVertexByIndexedId(dataRuleNode.getProperty(CommonConstants.CODE_PROPERTY), VertexLabelConstants.DATA_RULE);
      Iterator<Vertex> vertexIterator = ruleNode.getVertices(Direction.IN, RelationshipLabelConstants.DATA_RULES).iterator();
      while (vertexIterator.hasNext()) {
        Vertex klassNode = vertexIterator.next();
        klassIds.add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
      dataRuleMap.put("klassIds", klassIds);
      dataRuleMap.put("physicalCatalogList", new ArrayList<>());
      dataRuleMap.remove("cid");
      list.add(dataRuleMap);
    }
    returnMap.put(DATA_RULE_RESPONSE_LIST, list);

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
    List<String> physicalCatalogIds = (List<String>) returnMap.get(IDataRule.PHYSICAL_CATALOG_IDS);
    if(physicalCatalogIds.isEmpty()) {
      physicalCatalogIds.addAll(Constants.PHYSICAL_CATALOG_IDS);
    }
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
    
    GetDataRuleUtils.fillAttributeRulesData(dataRule, attributes, referencedRuleList, getReferencedData);
    fillRuleViolationsData(dataRule, ruleViolations);
    GetDataRuleUtils.fillRoleRulesData(dataRule, roles);
    // fillTypeRulesData(dataRule, types);
    GetDataRuleUtils.fillRelationshipRulesData(dataRule, relationships);
    GetDataRuleUtils.fillTagRulesData(dataRule, tags);
    GetDataRuleUtils.fillNormalizationDataForMigration(dataRule, normalizations);
    GetDataRuleUtils.fillTypesData(dataRule, types);
    GetDataRuleUtils.fillTaxonomiesData(dataRule, taxonomies);
    GetDataRuleUtils.fillOrganizations(dataRule, organizations);
    GetDataRuleUtils.fillEndpoints(dataRule, endpoints);
    GetDataRuleUtils.fillLanguages(dataRule, languages);
    
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
  
  private static void fillRuleViolationsData(Vertex dataRule, List<Map<String, Object>> ruleViolations)
  {
    Iterable<Vertex> ruleViolationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_VIOLATION_LINK);
    for (Vertex ruleViolation : ruleViolationVertices) {
      Map<String, Object> ruleViolationMap = new HashMap<>();
      Set<String> propertyKeys = ruleViolation.getPropertyKeys();
      for(String key: propertyKeys) {
        if(key.startsWith("description__")) {
          ruleViolationMap.put("description", ruleViolation.getProperty(key));
        }
        else {
          ruleViolationMap.put(key, ruleViolation.getProperty(key));
        }
      }
      ruleViolationMap.remove("cid");
      ruleViolations.add(ruleViolationMap);
    }
  }

  private void fillInfo(int from, int size, Map<String, Object> returnMap, String vertexType)
  {
    long count;
    List<Object> list = new ArrayList<>();
    String countQuery = "select count(*) from " + vertexType;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put(COUNT, count);
    
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + vertexType + " skip  " + from + " limit " + size)).execute();
    
    for (Vertex tagVertex : searchResults) {
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, tagVertex.getProperty(CommonConstants.CODE_PROPERTY));
      object.put(TYPE, tagVertex.getProperty(CommonConstants.TYPE_PROPERTY));
      list.add(object);
    }
    returnMap.put(LIST, list);
  }
  
  private void fillKlassInfo(int from, int size, Map<String, Object> returnMap)
  {
    long count;
    List<Object> list = new ArrayList<>();
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where isTaxonomy is null";
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put(COUNT, count);
    
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where isTaxonomy is null skip  " + from + " limit " + size))
        .execute();
    
    for (Vertex tagVertex : searchResults) {
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, tagVertex.getProperty(CommonConstants.CODE_PROPERTY));
      object.put(TYPE, tagVertex.getProperty(CommonConstants.TYPE_PROPERTY));
      list.add(object);
    }
    returnMap.put(LIST, list);
  }
  
  private void fillTags(int from, int size, Map<String, Object> returnMap)
  {
    long count;
    List<Object> object = new ArrayList<>();
    
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TAG + " where " + ITag.IS_ROOT + " = true";
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put(COUNT, count);
    
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from " + VertexLabelConstants.ENTITY_TAG + " where " + ITag.IS_ROOT + " = true skip  " + from + " limit " + size))
        .execute();
    
    for (Vertex tagVertex : searchResults) {
      Map<String, Object> tag = new HashMap<>();
      tag.put(CODE, tagVertex.getProperty(CommonConstants.CODE_PROPERTY));
      tag.put(TYPE, tagVertex.getProperty(CommonConstants.TAG_TYPE_PROPERTY));
      
      String rid = tagVertex.getId().toString();
      Iterable<Vertex> resultIterable = UtilClass.getGraph().command(new OCommandSQL("select expand(in ('Child_Of')) from " + rid
          + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc")).execute();
      List<Object> childTags = new ArrayList<>();
      for (Vertex childTagNode : resultIterable) {
        Map<String, Object> childMap = new HashMap<>();
        childMap.put(CODE, childTagNode.getProperty(CommonConstants.CODE_PROPERTY));
        childMap.put(TYPE, childTagNode.getProperty(CommonConstants.TYPE_PROPERTY));
        childTags.add(childMap);
      }
      tag.put(CHILDRENS, childTags);
      object.add(tag);
      
    }
    returnMap.put(LIST, object);
  }
  
  private void fillUsers(int from, int size, Map<String, Object> returnMap)
  {
    long count;
    List<Object> list = new ArrayList<>();
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_USER;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put("count", count);
    
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER + " skip  " + from + " limit " + size)).execute();
    
    for (Vertex tagVertex : searchResults) {
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, tagVertex.getProperty(CommonConstants.CODE_PROPERTY));
      object.put(USERNAME, tagVertex.getProperty(CommonConstants.USER_NAME_PROPERTY));
      list.add(object);
    }
    returnMap.put(LIST, list);
  }
  
  private void fillTaxonomy(int from, int size, Map<String, Object> returnMap)
  {
    long count;
    List<Object> list = new ArrayList<>();
    
    String countQuery = "select count(*) from Root_Klass_Taxonomy where out('Child_Of').size() = 0 and (isTaxonomy is null or isTaxonomy = true)";
    
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put("count", count);
    
    String query = "select  from Root_Klass_Taxonomy where out('Child_Of').size() = 0 and (isTaxonomy is null or isTaxonomy = true) skip "
        + from + " limit " + size;
    
    Iterable<Vertex> searchResults = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex taxonomyVertex : searchResults) {
      
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, taxonomyVertex.getProperty(CommonConstants.CODE_PROPERTY));
      object.put(TYPE, taxonomyVertex.getProperty(CommonConstants.BASE_TYPE));
      fillTaxonomyChildren(taxonomyVertex, object);
      list.add(object);
    }
    returnMap.put(LIST, list);
  }
  
  private void fillTaxonomyChildren(Vertex taxonomyVertex, Map<String, Object> taxonomy)
  {
    String childQuery = "select expand(IN('Child_Of') [isTaxonomy = true or isTaxonomy is null])  from  " + taxonomyVertex.getId();
    Iterable<Vertex> childrens = UtilClass.getGraph().command(new OCommandSQL(childQuery)).execute();
    List<Object> taxonomyChildrens = new ArrayList<Object>();
    for (Vertex children : childrens) {
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, children.getProperty(CommonConstants.CODE_PROPERTY));
      object.put(TYPE, children.getProperty(CommonConstants.BASE_TYPE));
      fillTaxonomyChildren(children, object);
      taxonomyChildrens.add(object);
    }
    taxonomy.put(CHILDRENS, taxonomyChildrens);
  }
  
  private void fillLanguage(int from, int size, Map<String, Object> returnMap)
  {
    long count;
    List<Object> list = new ArrayList<>();
    
    String countQuery = "select count(*) from Language  where out('Child_Of').size() = 0";
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put("count", count);
    
    String query = "select  from Language  where out('Child_Of').size() = 0 skip " + from + " limit " + size;
    
    Iterable<Vertex> searchResults = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex languageVertex : searchResults) {
      
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, languageVertex.getProperty(CommonConstants.CODE_PROPERTY));
      fillLanguageChildren(languageVertex, object);
      list.add(object);
    }
    returnMap.put(LIST, list);
  }
  
  private void fillLanguageChildren(Vertex languageVertex, Map<String, Object> language)
  {
    String childQuery = "select expand(IN('Child_Of')) from  " + languageVertex.getId();
    Iterable<Vertex> childrens = UtilClass.getGraph().command(new OCommandSQL(childQuery)).execute();
    List<Object> LanguageChildrens = new ArrayList<Object>();
    for (Vertex children : childrens) {
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, children.getProperty(CommonConstants.CODE_PROPERTY));
      fillTaxonomyChildren(children, object);
      LanguageChildrens.add(object);
    }
    language.put(CHILDRENS, LanguageChildrens);
  }
  
  private void fillAttributionTaxonomy(int from, int size, Map<String, Object> returnMap)
  {
    long count;
    List<Object> list = new ArrayList<>();
    
    String countQuery = "select count(*) from Attribution_Taxonomy where out('Child_Of').size() = 0 and (isTaxonomy is null or isTaxonomy = true)";
    
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put("count", count);
    
    String query = "select  from Attribution_Taxonomy where out('Child_Of').size() = 0 and (isTaxonomy is null or isTaxonomy = true) skip "
        + from + " limit " + size;
    
    Iterable<Vertex> searchResults = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex taxonomyVertex : searchResults) {
      
      Map<String, Object> object = new HashMap<>();
      object.put(CODE, taxonomyVertex.getProperty(CommonConstants.CODE_PROPERTY));
      object.put(TYPE, taxonomyVertex.getProperty(CommonConstants.BASE_TYPE));
      fillTaxonomyChildren(taxonomyVertex, object);
      list.add(object);
    }
    returnMap.put(LIST, list);
  }
  
}
