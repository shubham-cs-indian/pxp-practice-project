package com.cs.config.strategy.plugin.usecase.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.user.IGetRolesOrUsersDataByPartnerIdModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetRolesOrUsersDataByPartnerId extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH          = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.CODE_PROPERTY,
      CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_USER = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.CODE_PROPERTY,
      CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      IUser.FIRST_NAME, IUser.LAST_NAME);
  
  public GetRolesOrUsersDataByPartnerId(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRolesOrUsersDataByPartnerId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String searchColumn = (String) requestMap.get(IGetConfigDataRequestModel.SEARCH_COLUMN);
    String searchText = (String) requestMap.get(IGetConfigDataRequestModel.SEARCH_TEXT);
    searchText = searchText.replace("'", "\\'");
    Map<String, Object> entities = (Map<String, Object>) requestMap
        .get(IGetConfigDataRequestModel.ENTITIES);
    String organizationId = (String) requestMap
        .get(IGetRolesOrUsersDataByPartnerIdModel.ORGANIZATION_ID);
    Map<String, Object> rolesResponse = getRoles(searchColumn, searchText, entities,
        organizationId);
    Map<String, Object> usersResponse = getUsers(searchColumn, searchText, entities,
        organizationId);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetConfigDataResponseModel.ROLES, rolesResponse);
    responseMap.put(IGetConfigDataResponseModel.USERS, usersResponse);
    
    return responseMap;
  }
  
  private Map<String, Object> getRoles(String searchColumn, String searchText,
      Map<String, Object> entities, String organizationId)
  {
    Map<String, Object> rolesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ROLES);
    Map<String, Object> rolesResponse = null;
    if (rolesRequestInfo != null) {
      rolesResponse = fetchEntities(rolesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_ROLE, organizationId);
    }
    return rolesResponse;
  }
  
  private List<String> getFieldsToFetch(String entityLabel)
  {
    switch (entityLabel) {
      case VertexLabelConstants.ENTITY_TYPE_USER:
        return FIELDS_TO_FETCH_FOR_USER;
      default:
        return FIELDS_TO_FETCH;
    }
  }
  
  private Map<String, Object> fetchEntities(Map<String, Object> entityRequestInfo,
      String searchColumn, String searchText, String entityLabel, String organizationId)
  {
    Long from = Long.valueOf(entityRequestInfo.get(IGetConfigDataEntityPaginationModel.FROM)
        .toString());
    Long size = Long.valueOf(entityRequestInfo.get(IGetConfigDataEntityPaginationModel.SIZE)
        .toString());
    String sortBy = (String) entityRequestInfo.get(IGetConfigDataEntityPaginationModel.SORT_BY);
    String sortOrder = (String) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.SORT_ORDER);
    List<String> types = (List<String>) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.TYPES);
    Map<String, Object> properties = (Map<String, Object>) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.PROPERTIES);
    Long totalCount = getTotalCount(searchColumn, searchText, entityLabel, organizationId, types);
    
    Iterable<Vertex> searchResults = getVerticesAccordingToRequest(searchColumn, searchText, from,
        size, sortBy, sortOrder, properties, entityLabel, organizationId, types);
    List<Map<String, Object>> entitiesList = new ArrayList<>();
    for (Vertex searchResult : searchResults) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(getFieldsToFetch(entityLabel),
          searchResult);
      entitiesList.add(entityMap);
    }
    
    Map<String, Object> entitiesResponse = new HashMap<>();
    entitiesResponse.put(IGetConfigDataEntityResponseModel.FROM, from);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.SIZE, size);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.LIST, entitiesList);
    
    return entitiesResponse;
  }
  
  private Iterable<Vertex> getVerticesAccordingToRequest(String searchColumn, String searchText,
      Long from, Long size, String sortBy, String sortOrder, Map<String, Object> properties,
      String entityLabel, String systemLevelId, List<String> types)
  {
    String query = generateGetQuery(searchColumn, searchText, from, size, sortBy, sortOrder,
        properties, entityLabel, systemLevelId, types);
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    return searchResults;
  }
  
  private Long getTotalCount(String searchColumn, String searchText, String entityLabel,
      String systemLevelId, List<String> types)
  {
    String query = null;
    String orgCondition = "in('" + RelationshipLabelConstants.ORGANIZATION_ROLE_LINK + "')." + CommonConstants.CODE_PROPERTY
        + " = '" + systemLevelId + "'" + " AND ";
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_ROLE)) {
      query = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ROLE + " where " + 
               (systemLevelId == null ? "" : orgCondition) + IRole.IS_BACKGROUND_ROLE + "= false";
    }
    
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_USER)) {
      query = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_USER + " where "
          + (systemLevelId == null ? "" : "out('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN + "')." + orgCondition) + IUser.IS_BACKGROUND_USER + "= false";
    }
    return EntityUtil.executeCountQueryToGetTotalCount(query);
  }
  
  private String generateGetQuery(String searchColumn, String searchText, Long from, Long size,
      String sortBy, String sortOrder, Map<String, Object> properties, String entityLabel,
      String systemLevelId, List<String> types)
  {
    String query = null;
    String orgCondition = "in('" + RelationshipLabelConstants.ORGANIZATION_ROLE_LINK + "')." + CommonConstants.CODE_PROPERTY
        + " = '" + systemLevelId + "'" + " AND ";
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_ROLE)) {
      query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROLE + " where " +
          (systemLevelId == null ? "" : orgCondition) + IRole.IS_BACKGROUND_ROLE + "= false";
    }
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_USER)) {
      
      query = "select from "  + VertexLabelConstants.ENTITY_TYPE_USER + " where "
          + (systemLevelId == null ? "" : "out('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN + "')." + orgCondition) + IUser.IS_BACKGROUND_USER + "= false";
    }
    String queryParameters = getQueryParametersInString(searchColumn, searchText, from, size,
        sortBy, sortOrder);
    
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      
      if (query.endsWith(" AND ")) {
        int lastIndexOf = query.lastIndexOf(" AND ");
        query = query.substring(0, lastIndexOf);
      }
      
      if (query.endsWith(" WHERE ")) {
        queryParameters = queryParameters.replaceFirst(" AND ", "");
      }
    }
    else {
      
      if (query.endsWith(" AND ")) {
        int lastIndexOf = query.lastIndexOf(" AND ");
        query = query.substring(0, lastIndexOf);
      }
      
      if (query.endsWith(" WHERE ")) {
        int lastIndexOf = query.lastIndexOf(" WHERE ");
        query = query.substring(0, lastIndexOf);
      }
    }
    
    query += queryParameters;
    return query;
  }
  
  private String getQueryParametersInString(String searchColumn, String searchText, Long from,
      Long size, String sortBy, String sortOrder)
  {
    String queryParameters = "";
    
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      queryParameters += " AND " + searchColumn + " like '%" + searchText + "%'";
    }
    
    if (!UtilClass.isStringNullOrEmpty(sortBy)) {
      sortBy = EntityUtil.getLanguageConvertedField(sortBy);
      if (UtilClass.isStringNullOrEmpty(sortOrder)) {
        sortOrder = "asc";
      }
      queryParameters += " order by " + sortBy + " " + sortOrder;
    }
    
    if (from != null && size != null) {
      queryParameters += " skip " + from + " limit " + size;
    }
    
    return queryParameters;
  }
  
  private Map<String, Object> getUsers(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> rolesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.USERS);
    Map<String, Object> rolesResponse = null;
    if (rolesRequestInfo != null) {
      rolesResponse = fetchEntities(rolesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_USER, systemLevelId);
      List<Map<String, Object>> entitiesList = (List<Map<String, Object>>) rolesResponse
          .get(IGetConfigDataEntityResponseModel.LIST);
      for (Map<String, Object> entity : entitiesList) {
        entity.put(CommonConstants.LABEL_PROPERTY,
            entity.remove(IUser.FIRST_NAME) + " " + entity.remove(IUser.LAST_NAME));
      }
    }
    return rolesResponse;
  }
}
