package com.cs.config.strategy.plugin.usecase.variantcontext;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
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

@SuppressWarnings("unchecked")
public class GetAllVariantContext extends AbstractOrientPlugin {
  
  public GetAllVariantContext(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllVariantContext/*" };
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
    List<String> types = (List<String>) requestMap.get(IConfigGetAllRequestModel.TYPES);
    Long count = new Long(0);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    StringBuilder typeQuery = UtilClass.getTypeQueryWithoutANDOperator(types, IVariantContext.TYPE);
    StringBuilder condition = EntityUtil.getConditionQuery(searchQuery, typeQuery);
    
    String query = "select from " + VertexLabelConstants.VARIANT_CONTEXT + condition + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.VARIANT_CONTEXT + condition;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllVariantContextsResponseModel.LIST, list);
    responseMap.put(IGetAllVariantContextsResponseModel.COUNT, count);
    return responseMap;
  }
  
  private static Map<String, Object> getContexts(Vertex contextNode)
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IVariantContext.LABEL, IVariantContext.TYPE, IVariantContext.CODE, IVariantContext.ICON,
        IVariantContext.IS_STANDARD);
    Map<String, Object> returnMap = UtilClass.getMapFromVertex(fieldsToFetch, contextNode);
    return returnMap;
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
    List<Map<String, Object>> contextsToReturn = new ArrayList<>();
    for (Vertex contextNode : searchResults) {
      Map<String, Object> taskMap = getContexts(contextNode);
      contextsToReturn.add(taskMap);
    }
    return contextsToReturn;
  }
}
