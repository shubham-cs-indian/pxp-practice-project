package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMappings extends AbstractOrientPlugin {
  
  public GetMappings(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
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
    Long count = new Long(0);
    
    searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    String query = "select from " + VertexLabelConstants.PROPERTY_MAPPING + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.PROPERTY_MAPPING
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllMappingsResponseModel.LIST, list);
    responseMap.put(IGetAllMappingsResponseModel.COUNT, count);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllMappings/*" };
  }
  
  /**
   * @author Ajit
   * @param query
   * @return
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> mappingsToReturn = new ArrayList<>();
    for (Vertex mappingNode : searchResults) {
      Map<String, Object> mappingMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IEndpoint.LABEL, IEndpoint.CODE,
              IMapping.MAPPING_TYPE),
          mappingNode);
      mappingsToReturn.add(mappingMap);
    }
    return mappingsToReturn;
  }
}
