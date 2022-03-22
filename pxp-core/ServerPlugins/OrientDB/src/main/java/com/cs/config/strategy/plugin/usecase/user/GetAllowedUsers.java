package com.cs.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetAllowedUsers extends AbstractOrientPlugin {
  
  protected final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IUser.FIRST_NAME, IUser.LAST_NAME);
  
  public GetAllowedUsers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> returnList = new ArrayList<>();
    
    Long from = Long.valueOf(requestMap.get(IGetAllowedUsersRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetAllowedUsersRequestModel.SIZE)
        .toString());
    String searchText = (String) requestMap.get(IGetAllowedUsersRequestModel.SEARCH_TEXT);
    
    String concatedLabel = "$label";
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, concatedLabel);
    StringBuilder finalQuery = new StringBuilder();
    
    if (searchQuery.length() != 0) {
      finalQuery.append(" and " + searchQuery);
    }
    
    String query = "SELECT *, " + concatedLabel + " FROM " + VertexLabelConstants.ENTITY_TYPE_USER
        + " let " + concatedLabel + " = " + IUser.FIRST_NAME + ".append(" + IUser.LAST_NAME
        + ") where out('" + RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN + "').size() = 0 "
        + finalQuery + " order by " + concatedLabel + " asc skip " + from + " limit " + size;
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex userNode : searchResults) {
      Map<String, Object> userMap = UtilClass.getMapFromVertex(fieldsToFetch, userNode);
      String label = (String) userMap.get(IUser.FIRST_NAME) + " "
          + (String) userMap.get(IUser.LAST_NAME);
      userMap.put(IUser.LABEL, label);
//      UserUtils.getPreferredLanguages(userMap, userNode);
      returnList.add(userMap);
    }
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IListModel.LIST, returnList);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedUsers/*" };
  }
}
