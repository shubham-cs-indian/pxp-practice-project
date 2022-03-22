package com.cs.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.attribute.util.GetGridAttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
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

@SuppressWarnings("unchecked")
public class GetGridAttributes extends AbstractOrientPlugin {
  
  public GetGridAttributes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridAttributes/*" };
  }
  
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
    List<String> types = (List<String>) requestMap.get(IConfigGetAllRequestModel.TYPES);
    Boolean isStandard = (Boolean) (requestMap.get(IConfigGetAllRequestModel.IS_STANDARD));
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    isStandard = (isStandard == null) ? false : isStandard;
    StringBuilder isStandarQuery = new StringBuilder();
    if (isStandard) {
      isStandarQuery.append(" isStandard = " + isStandard);
    }
    
    StringBuilder typeQuery = UtilClass.getTypeQueryWithoutANDOperator(types, IAttribute.TYPE);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, isStandarQuery,
        typeQuery);
    
    Long count = new Long(0);
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> configDetails = GetGridAttributeUtils.getConfigDetails(list);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridAttributesResponseModel.ATTRIBUTE_LIST, list);
    responseMap.put(IGetGridAttributesResponseModel.COUNT, count);
    responseMap.put(IGetGridAttributesResponseModel.CONFIG_DETAILS, configDetails);
    return responseMap;
  }
  
  /**
   * Osho
   *
   * @param query
   * @param isSearchQuery
   * @return List<Map<String, Object> listOfAttributesToBeReturned
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> attributesToReturn = new ArrayList<>();
    for (Vertex searchResult : searchResults) {
      Map<String, Object> attributeMap = AttributeUtils.getAttributeMap(searchResult);
      attributesToReturn.add(attributeMap);
    }
    return attributesToReturn;
  }
}
