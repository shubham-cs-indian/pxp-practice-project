package com.cs.config.strategy.plugin.usecase.ssoconfiguration;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetGridSSOConfiguration extends AbstractOrientPlugin {
  
  public GetGridSSOConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridSSOConfiguration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> ssoSetting = (HashMap<String, Object>) requestMap
        .get(CommonConstants.SSO_SETTING);
    Long from = Long.valueOf(ssoSetting.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(ssoSetting.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = (String) ssoSetting.get(IConfigGetAllRequestModel.SORT_BY);
    String sortOrder = (String) ssoSetting.get(IConfigGetAllRequestModel.SORT_ORDER);
    String searchText = (String) ssoSetting.get(IConfigGetAllRequestModel.SEARCH_TEXT);
    String searchColumn = (String) ssoSetting.get(IConfigGetAllRequestModel.SEARCH_COLUMN);
    String organizationId = (String) ssoSetting
        .remove(ICreateSSOConfigurationModel.ORGANIZATION_ID);
    
    StringBuilder searchQuery = getSearchQuery(searchText, searchColumn);
    String getOrganizationQuery = "select from " + VertexLabelConstants.ORGANIZATION
        + " where code = '" + organizationId + "'";
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    String edgeLabel = RelationshipLabelConstants.ORGANIZATION_SSO_LINK;
    
    String query = "select expand(out('" + edgeLabel + "')" + searchQuery + ") from ("
        + getOrganizationQuery + ")" + " order by " + sortBy + " " + sortOrder + " skip " + from
        + " limit " + size;
    List<Map<String, Object>> ssoList = executeQueryAndPrepareResponse(query);
    
    Long count = new Long(0);
    String countQuery = "select count(*) from (select expand(out('" + edgeLabel + "')" + searchQuery
        + ") from (" + getOrganizationQuery + "))";
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridSSOConfigurationResponseModel.SSO_CONFIGURATION_LIST, ssoList);
    responseMap.put(IGetGridSSOConfigurationResponseModel.COUNT, count);
    
    return responseMap;
  }
  
  private StringBuilder getSearchQuery(String searchText, String searchColumn)
  {
    StringBuilder query = new StringBuilder();
    if (searchText != null && !searchText.isEmpty()) {
      searchText = searchText.replace("'", "\\'");
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      query.append("[" + searchColumn + " like '%" + searchText + "%' ]");
    }
    return query;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> ssoList = new ArrayList<>();
    
    for (Vertex ssoVertex : searchResults) {
      HashMap<String, Object> ssoMap = (HashMap<String, Object>) UtilClass
          .getMapFromVertex(new ArrayList<>(), ssoVertex);
      ssoList.add(ssoMap);
    }
    return ssoList;
  }
}
