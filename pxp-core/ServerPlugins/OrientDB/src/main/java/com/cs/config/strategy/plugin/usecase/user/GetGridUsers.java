package com.cs.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetGridUsers extends AbstractOrientPlugin {
  
  public GetGridUsers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridUsers/*" };
  }
  
  public static List<String> fieldsToFetch = Arrays.asList(IRole.ID, IRole.LABEL, IRole.CODE);
  public static List<String> organizationFieldsToFetch = Arrays.asList(IRole.TYPE, IRole.LABEL);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = new ArrayList<>();
    
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder finalSerachQuery = new StringBuilder();
    if (searchQuery.length() != 0) {
      finalSerachQuery.append(" OR ");
      finalSerachQuery.append(searchQuery);
    }
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    Long count = new Long(0);
    
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_USER + " where ("
        + IUser.LAST_NAME + " like '%" + searchText + "%' OR " + IUser.USER_NAME + " like '%"
        + searchText + "%' " + finalSerachQuery + " ) order by " + sortBy + " " + sortOrder
        + " skip " + from + " limit " + size;
    
    Map<String, Object> referencedRoles = new HashMap<>();
    list = executeQueryAndPrepareResponse(query, referencedRoles);
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_USER + " where ("
        + IUser.LAST_NAME + " like '%" + searchText + "%' OR " + IUser.USER_NAME + " like '%"
        + searchText + "%' " + finalSerachQuery + ")";
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridUsersResponseModel.USERS_LIST, list);
    responseMap.put(IGetGridUsersResponseModel.COUNT, count);
    responseMap.put(IGetGridUsersResponseModel.REFERENCED_ROLES, referencedRoles);
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query,
      Map<String, Object> referenceRoles) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> usersToReturn = new ArrayList<>();
    
    for (Vertex userVertex : searchResults) {
      Map<String, Object> userMap = UtilClass.getMapFromNode(userVertex);
      userMap.put(IConfigMasterEntity.LABEL,
          userMap.get(IUser.FIRST_NAME) + " " + userMap.get(IUser.LAST_NAME));
      userMap.remove(IUser.PASSWORD);
      UserUtils.getPreferredLanguages(userMap, userVertex);
      addRoleTypeFieldToUserMap(userVertex, userMap, referenceRoles);
      usersToReturn.add(userMap);
    }
    return usersToReturn;
  }
  
  private void addRoleTypeFieldToUserMap(Vertex userVertex, Map<String, Object> userMap,
      Map<String, Object> referencedRoles)
  {
    
    Iterator<Edge> userOutRelationships = userVertex
        .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN)
        .iterator();
    
    if (userOutRelationships.hasNext()) {
      Vertex roleNode = userOutRelationships.next()
          .getVertex(Direction.IN);
      fillOrganizationDetails(roleNode, userMap);
      String roleId = UtilClass.getCodeNew(roleNode);
      if (!referencedRoles.containsKey(roleId)) {
        referencedRoles.put(roleId, UtilClass.getMapFromVertex(fieldsToFetch, roleNode));
      }
      userMap.put(IUserModel.ROLE_ID, roleId);
    }
  }
  
  private void fillOrganizationDetails(Vertex roleNode, Map<String, Object> userMap)
  {
    Iterator<Edge> roleInRelationships = roleNode
        .getEdges(Direction.IN, RelationshipLabelConstants.ORGANIZATION_ROLE_LINK)
        .iterator();
    
    if (roleInRelationships.hasNext()) {
      Vertex organizationNode = roleInRelationships.next()
          .getVertex(Direction.OUT);
      Map<String, Object> organizationDetails = UtilClass.getMapFromVertex(organizationFieldsToFetch, organizationNode);
      userMap.put(IUserModel.ORGANIZATION_NAME, organizationDetails.get(IRole.LABEL));
      userMap.put(IUserModel.ORGANIZATION_TYPE, organizationDetails.get(IRole.TYPE));
    }
  }
}
