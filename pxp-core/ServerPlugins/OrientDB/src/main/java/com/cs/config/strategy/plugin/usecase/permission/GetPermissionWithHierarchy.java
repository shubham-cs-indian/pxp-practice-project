package com.cs.config.strategy.plugin.usecase.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetPermissionWithHierarchy extends AbstractOrientPlugin {
  
  public GetPermissionWithHierarchy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPermissionWithHierarchy/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    
    String roleId = (String) requestMap.get(IPermissionWithHierarchyRequestModel.ROLE_ID);
    String query = prepareQuery(requestMap);
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    List<Map<String, Object>> permissionList = new ArrayList<Map<String, Object>>();
    for (Vertex vertex : resultIterable) {
      String vertexId = UtilClass.getCodeNew(vertex);
      Map<String, Object> permissionMap = new HashMap<String, Object>();
      permissionMap.put(IGetPermissionWithHierarchyModel.ID, vertexId);
      String label = (String) UtilClass.getValueByLanguage(vertex, CommonConstants.LABEL_PROPERTY);
      permissionMap.put(IGetPermissionWithHierarchyModel.LABEL, label);
      permissionMap.put(IGetPermissionWithHierarchyModel.CODE, vertex.getProperty(IKlass.CODE));
      permissionMap.put(IGetPermissionWithHierarchyModel.IS_NATURE,
          vertex.getProperty(IKlass.IS_NATURE));
      permissionMap.put(IGetPermissionWithHierarchyModel.TYPE, vertex.getProperty(IKlass.TYPE));
      permissionMap.put(IGetPermissionWithHierarchyModel.NATURE_TYPE,
          vertex.getProperty(IKlass.NATURE_TYPE));
      Map<String, Object> globalPermission = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(vertexId, roleId);
      permissionMap.put(IGetPermissionWithHierarchyModel.PERMISSION, globalPermission);
      permissionList.add(permissionMap);
    }
    responseMap.put(IListModel.LIST, permissionList);
    return responseMap;
  }
  
  private String prepareQuery(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(IPermissionWithHierarchyRequestModel.ID);
    id = id == null ? "-1" : id;
    String roleId = (String) requestMap.get(IPermissionWithHierarchyRequestModel.ROLE_ID);
    String entityType = (String) requestMap.get(IPermissionWithHierarchyRequestModel.ENTITY_TYPE);
    Long from = Long.valueOf(requestMap.get(IPermissionWithHierarchyRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IPermissionWithHierarchyRequestModel.SIZE)
        .toString());
    String searchText = (String) requestMap.get(IPermissionWithHierarchyRequestModel.SEARCH_TEXT);
    
    String labelProperty = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    String vertexLabel = getVertexTypeByEntityType(entityType);
    String taxonomyType = (String) requestMap.get(IPermissionWithHierarchyRequestModel.TAXONOMY_TYPE);
    
    // only that ids which are added in target of role or organization(i.e.
    // having readPermission)
    // if empty consider all
    Set<String> idsWithRP = getIdsWithReadPermission(roleId, entityType);
    
    String query = "";
    // root level
    if (id.equals("-1")) {
      String searchQuery = "";
      String conditionQuery = "";
      String queryToExcludeKlasses = "";
      
      query += "SELECT FROM " + vertexLabel;
      
      // for search
      if (searchText != null && !searchText.isEmpty()) {
        searchQuery = labelProperty + " like '%" + searchText + "%' ";
      }
      
      if (idsWithRP.isEmpty() || CommonConstants.MINOR_TAXONOMY.equals(taxonomyType)) {
        // condition like childOf outEdges must be 0 and taxonomy conditions
        conditionQuery = getConditionQueryForRootLevel(id, entityType, taxonomyType);
      }
      else {
        // only those ids having RP
        conditionQuery = " code in " + EntityUtil.quoteIt(idsWithRP);
      }
      
      if (vertexLabel.equals(VertexLabelConstants.ENTITY_TYPE_KLASS)) {
        queryToExcludeKlasses = CommonConstants.CODE_PROPERTY + " NOT IN "
            + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN);
      }
      
      StringBuilder finalConditionQueryBuilder = EntityUtil.getConditionQuery(
          new StringBuilder(searchQuery), new StringBuilder(conditionQuery),
          new StringBuilder(queryToExcludeKlasses));
      
      String finalConditionQuery = finalConditionQueryBuilder.toString();
      if (!finalConditionQuery.isEmpty()) {
        query += finalConditionQuery;
      }
    }
    else { // non-root level (not for event and task)
      Vertex vertex = UtilClass.getVertexByIndexedId(id, vertexLabel);
      if (searchText != null && !searchText.isEmpty()) {
        query += "SELECT FROM (SELECT expand(in( '"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')) from " + vertex.getId()
            + ") WHERE " + labelProperty + " like '%" + searchText + "%' ";
      }
      else {
        query += "SELECT FROM (SELECT expand(in( '"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')) from " + vertex.getId()
            + ")";
      }
    }
    
    // pagination and sort
    query += " ORDER BY " + labelProperty + " asc SKIP " + from + " LIMIT " + size;
    
    return query;
  }
  
  private Set<String> getIdsWithReadPermission(String roleId, String entityType) throws Exception
  {
    if (entityType.equals(CommonConstants.TASK)) {
      return new HashSet<>();
    }
    
    Vertex roleNode = UtilClass.getVertexByIndexedId(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    if (entityType.equals(CommonConstants.MASTER_TAXONOMY)) {
      return GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode);
    }
    
    return GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode);
  }
  
  private String getVertexTypeByEntityType(String entityType)
  {
    switch (entityType) {
      case CommonConstants.ARTICLE_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case CommonConstants.ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      case CommonConstants.TARGET:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case CommonConstants.SUPPLIER_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      case CommonConstants.TEXT_ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      case CommonConstants.MASTER_TAXONOMY:
        return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
      case CommonConstants.TASK:
        return VertexLabelConstants.ENTITY_TYPE_TASK;
      default:
        break;
    }
    return null;
  }
  
  private String getConditionQueryForRootLevel(String id, String entityType, String taxonomyType)
  {
    String query = "";
    switch (entityType) {
      case CommonConstants.MASTER_TAXONOMY:
        query += ITaxonomy.TAXONOMY_TYPE + " = '" + taxonomyType + "'";
        query += " AND outE('Child_Of').size() = 0 ";
        break;
    }
    
    return query;
  }
}
