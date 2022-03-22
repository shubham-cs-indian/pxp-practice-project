package com.cs.config.strategy.plugin.usecase.entityCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IConfigMap;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.EntityCountLabelConstants;
import com.cs.core.config.interactor.model.objectCount.IEntitySubTypeModel;
import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityCountModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetEntityCount extends AbstractOrientPlugin {
  
  public GetEntityCount(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEntityCount/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> entity = new HashMap<>();
    String entityType = (String) requestMap.get(IGetEntityTypeRequestModel.ENTITY_TYPE);
    
    List<Object> entityTypes = new ArrayList<>();
    Map<String, Long> entitySubTypeCount = new HashMap<>();
    Map<String, Long> entityTypeCount = new HashMap<>();
    Map<String, Object> entitySubType = new HashMap<>();
    Map<String, Object> responseMap = new HashMap<>();
    String entityQuery;
    
    switch (entityType) {
      
      case EntityCountLabelConstants.ARTICLE:
        
        entityQuery = "select natureType, count(natureType) from " + VertexLabelConstants.ENTITY_TYPE_KLASS
        + " where isAbstract = false " + " and code != 'golden_article_klass' group by natureType";
        executeEntityQueryAndPrepareEntitySubTypeMap(entitySubTypeCount, entityTypeCount, entitySubType, 
            entityQuery, "natureType");
        
        break;
      
      case EntityCountLabelConstants.ASSET:
        
        entityQuery = "select natureType, count(natureType) from " + VertexLabelConstants.ENTITY_TYPE_ASSET + " where isAbstract = false "
            + "group by natureType";
        
        executeEntityQueryAndPrepareEntitySubTypeMap(entitySubTypeCount, entityTypeCount, entitySubType, 
            entityQuery, "natureType");
        
        break;
      
      case EntityCountLabelConstants.CONTEXT:
        
        entityQuery = "Select type,count(type) from " + VertexLabelConstants.VARIANT_CONTEXT +" where "
            + " type != 'gtinVariant' group by type";
        
        executeQueryAndPrepareMap(entityTypeCount, entityQuery, "type");
        
        break;
      
      case EntityCountLabelConstants.DATA_GOVERNANCE:
        
        fillEntityCounFordataGovernance(entitySubTypeCount, entityTypeCount, entitySubType);
        break;
      
      case EntityCountLabelConstants.PROPERTY_GROUP:
        
        fillEntityCountForPropertyGroups(entityTypeCount);
        break;
      
      case EntityCountLabelConstants.PROPERTIES:
        
        fillEntityCountForProperties(entityTypeCount);
        break;
      
      case EntityCountLabelConstants.TEXT_ASSET:
        
        entityQuery = "select natureType, count(natureType) from " + VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET + 
        " where isAbstract = false group by natureType";
        
        executeEntityQueryAndPrepareEntitySubTypeMap(entitySubTypeCount, entityTypeCount, entitySubType, 
            entityQuery, "natureType");
        
        break;
      
      case EntityCountLabelConstants.LANGUAGE:
        
        entityQuery = "select count(*) from " + VertexLabelConstants.LANGUAGE;
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.LANGUAGES);
        
        break;
      
      case EntityCountLabelConstants.TARGET:
        
        entityQuery = "select natureType, count(natureType) from " + CommonConstants.TARGET
            + " where isAbstract = false group by natureType";
        executeEntityQueryAndPrepareEntitySubTypeMap(entitySubTypeCount, entityTypeCount, entitySubType, 
            entityQuery, "natureType");
        
        break;
      
      case EntityCountLabelConstants.DATA_INTEGRATION:
        
        entityQuery = "select count(*) from " + VertexLabelConstants.PROCESS_EVENT + " where workflowType != 'HIDDEN_WORKFLOW'";
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.WORKFLOW);
        
        entityQuery = "select count(*) from " + VertexLabelConstants.ENDPOINT;
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.ENDPOINT);
        
        entityQuery = "select count(*) from " + VertexLabelConstants.PROPERTY_MAPPING;
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.MAPPING);
        
        break;
      
      case EntityCountLabelConstants.SUPPLIER:
        
        entityQuery = "select type, count(type) from " + VertexLabelConstants.ORGANIZATION + " where "
            + "type != 'internal' group by type";
        
        executeQueryAndPrepareMap(entityTypeCount, entityQuery, "type");
        break;
      
      case EntityCountLabelConstants.BUSINESS_PARTNERS:
        
        entityQuery = "select count(*) from " + VertexLabelConstants.ORGANIZATION;
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.BUSINESS_PARTNERS);
        
        entityQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ROLE
            + " where isBackgroundRole = false and  code not in['approverrole', "
            + "'ownerrole', 'subscriberrole', 'contributorrole', 'reviewerrole', 'seniormanagerrole', " + "'assigneerole']";
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.ENTITY_TYPE_ROLE);
        
        entityQuery = "select count(*) from " + EntityCountLabelConstants.USER;
        executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.USER);
        
        break;
      
      case EntityCountLabelConstants.COLLABRATION:
        
        fillEntityCountForCollabration(entitySubTypeCount, entityTypeCount, entitySubType);
        break;
      
      case EntityCountLabelConstants.RELATIONSHIP:
        
        fillEntityCountForRelationship(entityTypeCount);
        break;
      
      case EntityCountLabelConstants.TAXONOMY:
        
        fillEntityCountForTaxonomy(entitySubTypeCount, entityTypeCount, entitySubType);
        break;
    }
    
    entity.put(IGetEntityCountModel.ENTITY_TYPE, entityType);
    entity.put(IGetEntityCountModel.ENTITY_SUB_TYPE, entitySubType);
    entity.put(IGetEntityCountModel.ENTITY_COUNT, entityTypeCount);
    
    entityTypes.add(entity);
    
    responseMap.put(IGetConfigEntityResponseModel.DATA_MODEL, entityTypes);
    return responseMap;
  }

  private void executeEntityQueryAndPrepareEntitySubTypeMap(Map<String, Long> entitySubTypeCount, Map<String, Long> entityTypeCount,
      Map<String, Object> entitySubType, String entityQuery, String type)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    Iterator<Vertex> iterator = countResult.iterator();
    
    Map<String, Object> entitySubTypeCountMap = new HashMap<>();
    
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      
      String entityType = (String) vertex.getProperty(type);
      
      if (entityType.isEmpty()) {
        
        Long attributionCount = entityTypeCount.get(EntityCountLabelConstants.ATTRIBUTION_CLASSES);
        entityTypeCount.put(EntityCountLabelConstants.ATTRIBUTION_CLASSES, attributionCount == null ? count : attributionCount + count);
      }else {
        
        entitySubTypeCount.put(entityType, count);
        Long natureClassesCount = entityTypeCount.get(EntityCountLabelConstants.NATURE_CLASSES);
        entityTypeCount.put(EntityCountLabelConstants.NATURE_CLASSES, natureClassesCount == null ? count : natureClassesCount + count);
        
      }
    }
    
    entitySubTypeCountMap.put(IEntitySubTypeModel.ENTITY_SUB_TYPE_COUNT, entitySubTypeCount);
    entitySubType.put(EntityCountLabelConstants.NATURE_CLASSES, entitySubTypeCountMap);
  }
  
  private void fillEntityCountForTaxonomy(Map<String, Long> entitySubTypeCount, Map<String, Long> entityTypeCount,
      Map<String, Object> entitySubType)
  {
    String entityQuery = "select count(*) from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY + " where isTaxonomy = true "
        + "and taxonomyType= '" + CommonConstants.MAJOR_TAXONOMY + "'";
    
    Map<String, Object> entitySubTypeCountMap = new HashMap<>();
    executeCountQueryAndPrepareMap(entitySubTypeCount, entityQuery, "Total Nodes");
    entitySubTypeCountMap.put(IEntitySubTypeModel.ENTITY_SUB_TYPE_COUNT, entitySubTypeCount);
    entitySubType.put(CommonConstants.MASTER_TAXONOMY, entitySubTypeCountMap);
    
    entityQuery = "select count(*) from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY + " where isTaxonomy = true "
        + "and outE('Child_Of').size() = 0 and taxonomyType= '" + CommonConstants.MAJOR_TAXONOMY + "'";
    
    executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, CommonConstants.MASTER_TAXONOMY);
    
    entityQuery = "select count(*) from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY + " where isTaxonomy = true "
        + "and outE('Child_Of').size() = 0 and taxonomyType= '" + CommonConstants.MINOR_TAXONOMY + "'";
    
    executeCountQueryAndPrepareMap(entityTypeCount, entityQuery, EntityCountLabelConstants.MINOR_TAXONOMY);
    
    entityQuery = "select count(*) from " + CommonConstants.ATTRIBUTION_TAXONOMY_KLASS + " where isTaxonomy = true " + "and taxonomyType= '"
        + CommonConstants.MINOR_TAXONOMY + "'";
    
    entitySubTypeCountMap = new HashMap<>();
    entitySubTypeCount = new HashMap<>();
    
    executeCountQueryAndPrepareMap(entitySubTypeCount, entityQuery, "Total Nodes");
    entitySubTypeCountMap.put(IEntitySubTypeModel.ENTITY_SUB_TYPE_COUNT, entitySubTypeCount);
    entitySubType.put(EntityCountLabelConstants.MINOR_TAXONOMY, entitySubTypeCountMap);
  }
  
  private void fillEntityCountForRelationship(Map<String, Long> entityTypeCount)
  {
    String entityQuery = "select isLite, count(isLite) from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP + " group by isLite";
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    Iterator<Vertex> iterator = countResult.iterator();
    
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      Boolean isLite = (Boolean) vertex.getProperty("isLite");
      
      if (isLite) {
        entityTypeCount.put(EntityCountLabelConstants.LITE_RELATIONSHIP, count);
      }
      else {
        entityTypeCount.put(EntityCountLabelConstants.RELATIONSHIP, count);
      }
    }
  }
  
  private void fillEntityCountForCollabration(Map<String, Long> entitySubTypeCount, Map<String, Long> entityTypeCount, 
      Map<String,Object> entitySubType)
  {
    String entityQuery = "select type, count(type) from " + VertexLabelConstants.ENTITY_TYPE_TASK + " group by type";
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex>  countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    Iterator<Vertex> iterator = countResult.iterator();
    
    Map<String, Object> entitySubTypeCountMap = new HashMap<>();
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      
      String type = (String) vertex.getProperty("type");
      entitySubTypeCount.put(type, count);
      Long taskCount = entityTypeCount.get(EntityCountLabelConstants.TASK);
      entityTypeCount.put(EntityCountLabelConstants.TASK, taskCount == null ? count : taskCount + count);
    }
    
    entitySubTypeCountMap.put(IEntitySubTypeModel.ENTITY_SUB_TYPE_COUNT, entitySubTypeCount);
    entitySubType.put(EntityCountLabelConstants.TASK, entitySubTypeCountMap);
  }

  private void executeCountQueryAndPrepareMap(Map<String, Long> entityTypeCount, String entityQuery, String type)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    Iterator<Vertex> iterator = countResult.iterator();
    
    if (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      
      entityTypeCount.put(type, count);
    }
  }
  
  private void fillEntityCountForProperties(Map<String, Long> entityTypeCount)
  {
    String entityQuery = "select type, count(type) from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " group by type";
    
    Iterable<Vertex> countResult;
    Iterator<Vertex> iterator;
    OrientGraph graph = UtilClass.getGraph();
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      
      String type = (String) vertex.getProperty("type");
      String attributeType = IConfigMap.getPropertyType(type).name();
      if(attributeType.equals(PropertyType.MEASUREMENT.name())) {
        Long existingCount = entityTypeCount.computeIfAbsent(attributeType, k -> 0L);
        entityTypeCount.put(attributeType, count + existingCount);
      }
      else if(!attributeType.equals(PropertyType.ASSET_ATTRIBUTE.name())) {
        entityTypeCount.put(attributeType, count);
      }
    }
    
    entityQuery = "select count(*) from " + CommonConstants.ATTRIBUTE + " where isStandard = true";
    
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      entityTypeCount.put(EntityCountLabelConstants.STANDARD_ATTRIBUTE, count);
    }
    
    entityQuery = "select tagType, count(tagType) from " + VertexLabelConstants.ENTITY_TAG + " group by tagType";
    
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    fillEntityCountForTags(entityTypeCount, iterator);
    
  }
  
  private void fillEntityCountForTags(Map<String, Long> entityTypeCount, Iterator<Vertex> iterator)
  {
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      
      String tagType = (String) vertex.getProperty("tagType");
      
      if (tagType == null) {
        continue;
      }
      if (tagType.equals("tag_type_boolean")) {
        
        Long booleanTagCount = entityTypeCount.get("Boolean");
        entityTypeCount.put(EntityCountLabelConstants.BOOLEAN, booleanTagCount != null ? booleanTagCount + count : count);
      }
      else if (tagType.equals(CommonConstants.YES_NEUTRAL_TAG_TYPE_ID) || tagType.equals(CommonConstants.YES_NEUTRAL_NO_TAG_TYPE_ID)
          || tagType.equals(CommonConstants.RANGE_TAG_TYPE_ID) || tagType.equals(CommonConstants.RULER_TAG_TYPE_ID)) {
        Long lovCount = entityTypeCount.get("LOV");
        entityTypeCount.put(EntityCountLabelConstants.LOV, lovCount != null ? lovCount + count : count);
      }
      else if (tagType.equals(CommonConstants.MASTER_TAG_TYPE_ID)) {
        entityTypeCount.put(EntityCountLabelConstants.MASTER, count);
      }
      else if (tagType.equals(CommonConstants.STATUS_TAG_TYPE_ID) || tagType.equals(CommonConstants.LISTING_TYPE_ID)
          || tagType.equals(CommonConstants.STATUS_TYPE_ID)) {
        Long statusCount = entityTypeCount.get(EntityCountLabelConstants.STATUS);
        entityTypeCount.put(EntityCountLabelConstants.STATUS, statusCount != null ? statusCount + count : count);
      }
    }
  }
  
  private void fillEntityCountForPropertyGroups(Map<String, Long> entityTypeCount)
  {
    String entityQuery;
    entityQuery = "select isForXRay, count(isForXRay) from " + VertexLabelConstants.PROPERTY_COLLECTION + " group by isForXRay";
    
    Iterable<Vertex> countResult;
    Iterator<Vertex> iterator;
    OrientGraph graph = UtilClass.getGraph();
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    while (iterator.hasNext()) {
      Vertex next = iterator.next();
      Long count = (Long) next.getProperty("count");
      
      Boolean isForXRay = (Boolean) next.getProperty("isForXRay");
      
      if (isForXRay) {
        entityTypeCount.put(EntityCountLabelConstants.X_RAY_COLLECTIONS, count);
      }
      else {
        entityTypeCount.put(EntityCountLabelConstants.PROPERTY_COLLECTIONS, count);
      }
      
    }
    
    entityQuery = "select count(*) from " + VertexLabelConstants.TAB + " where code != 'overview_tab'";
    
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    
    iterator = countResult.iterator();
    
    if (iterator.hasNext()) {
      entityTypeCount.put(EntityCountLabelConstants.TABS, iterator.next().getProperty("count"));
    }
  }
  
  private void fillEntityCounFordataGovernance(Map<String, Long> entitySubTypeCount, Map<String, Long> entityTypeCount,
      Map<String, Object> entitySubType)
  {
    Iterable<Vertex> countResult;
    Iterator<Vertex> iterator;
    String entityQuery = "select type, count(type) from " + VertexLabelConstants.DATA_RULE + " group by type";
    
    OrientGraph graph = UtilClass.getGraph();
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    Map<String, Object> entitySubTypeCountMap = new HashMap<>();
    
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      String type = (String) vertex.getProperty("type");
      
      entitySubTypeCount.put(type, count);
      Long rulesCount = (Long) entityTypeCount.get(EntityCountLabelConstants.RULES);
      
      entityTypeCount.put(EntityCountLabelConstants.RULES, rulesCount == null ? count : rulesCount + count);
    }
    
    entitySubTypeCountMap.put(IEntitySubTypeModel.ENTITY_SUB_TYPE_COUNT, entitySubTypeCount);
    
    if (!entitySubTypeCount.isEmpty()) {
      entitySubType.put(EntityCountLabelConstants.RULES, entitySubTypeCountMap);
    }
    
    entityQuery = "select count(*) from " + VertexLabelConstants.GOVERNANCE_RULE_KPI;
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    if (iterator.hasNext()) {
      entityTypeCount.put(EntityCountLabelConstants.KPIS, iterator.next().getProperty("count"));
    }
    
    entityQuery = "select count(*) from " + VertexLabelConstants.GOLDEN_RECORD_RULE;
    countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    iterator = countResult.iterator();
    
    if (iterator.hasNext()) {
      entityTypeCount.put(EntityCountLabelConstants.GOLDEN_RECORD_RULES, iterator.next().getProperty("count"));
    }
  }
  
  private void executeQueryAndPrepareMap(Map<String, Long> entityTypeCount, String entityQuery, String type)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> countResult = graph.command(new OCommandSQL(entityQuery)).execute();
    Iterator<Vertex> iterator = countResult.iterator();
    
    while (iterator.hasNext()) {
      Vertex vertex = iterator.next();
      Long count = (Long) vertex.getProperty("count");
      
      String entityType = (String) vertex.getProperty(type);
      if (entityType != null && !entityType.isEmpty()) {
        entityTypeCount.put(entityType, count);
      }
    }
  }
  
}
