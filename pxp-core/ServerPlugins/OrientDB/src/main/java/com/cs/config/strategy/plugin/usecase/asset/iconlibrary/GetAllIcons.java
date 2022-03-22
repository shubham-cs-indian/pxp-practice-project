package com.cs.config.strategy.plugin.usecase.asset.iconlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.asset.IGetAllIconsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetAllIcons extends AbstractOrientPlugin {
  
  public GetAllIcons(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllIcons/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> iconList = new ArrayList<>();
    Map<String, Object> responseMap = new HashMap<>();
    Long from = Long.valueOf(requestMap.get(IGetAllIconsRequestModel.FROM).toString());
    Long size = Long.valueOf(requestMap.get(IGetAllIconsRequestModel.SIZE).toString());
    String searchText = requestMap.get(IGetAllIconsRequestModel.SEARCH_TEXT).toString();
    String idToExclude = requestMap.get(IGetAllIconsRequestModel.ID_TO_EXCLUDE).toString();
    String sortBy = "modifiedOn";
    StringBuilder searchQuery = getSearchQuery(searchText);
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ICON + searchQuery;
    
    String idToExcludeQuery = getIdToExcludeQuery(idToExclude, searchQuery);
    query = query + idToExcludeQuery;
    query = query + " order by " + sortBy + " desc " + " skip " + from + " limit " + size;
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query)).execute();
    
    Long count=0L;
    String totalCountQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ICON + searchQuery;
    totalCountQuery = totalCountQuery + idToExcludeQuery;
    
    count = EntityUtil.executeCountQueryToGetTotalCount(totalCountQuery);
      
    for (Vertex iconNode : searchResults) {
      Map<String, Object> iconMap = new HashMap<>();
      iconMap.putAll(UtilClass.getMapFromNode(iconNode));
      iconList.add(iconMap);
    }
    responseMap.put(IGetAllIconsResponseModel.ICONS, iconList);
    responseMap.put(IGetAllIconsResponseModel.TOTAL_COUNT, count);
    responseMap.put(IGetAllIconsResponseModel.FROM, from);
    
    return responseMap;
  }
  
  private String getIdToExcludeQuery(String idToExclude, StringBuilder searchQuery) {
    String idToExcludeQuery = "";
    if(idToExclude != null && !idToExclude.isEmpty()) {
      if(searchQuery.length() > 0) {
        idToExcludeQuery = " and code <> '" + idToExclude + "'";
      }
      else {
        idToExcludeQuery = " where code <> '" + idToExclude + "'";
      }
    }
    
    return idToExcludeQuery;
  }
  
  private  StringBuilder getSearchQuery(String searchText)
  {
    String searchColumn = "label";
    StringBuilder query = new StringBuilder(); // search on label and code field
    if (searchText != null && !searchText.isEmpty()) {
      searchText = searchText.replace("'", "\\'");
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
    
      query.append(" where "+ searchColumn + " like '%" + searchText + "%' ");
      query.append(" or "+ " code " + " like '%" + searchText + "%' ");
    }
    return query;
  }
  
}
