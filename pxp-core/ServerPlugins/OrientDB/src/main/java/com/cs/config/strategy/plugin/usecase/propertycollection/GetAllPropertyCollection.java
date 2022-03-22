package com.cs.config.strategy.plugin.usecase.propertycollection;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;
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

public class GetAllPropertyCollection extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IPropertyCollection.LABEL, IPropertyCollection.ICON, IPropertyCollection.IS_STANDARD,
      IPropertyCollection.IS_FOR_X_RAY, IPropertyCollection.IS_DEFAULT_FOR_X_RAY,
      IPropertyCollection.CODE);
  
  public GetAllPropertyCollection(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllPropertyCollection/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Boolean isForXRay = (Boolean) requestMap
        .get(IGetAllPropertyCollectionRequestModel.IS_FOR_X_RAY);
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
    StringBuilder isForXrayQuery = new StringBuilder();
    if (isForXRay != null) {
      String isForXray = IPropertyCollection.IS_FOR_X_RAY + "=" + isForXRay;
      isForXrayQuery.append(isForXray);
    }
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, isForXrayQuery);
    
    String query = "select from " + VertexLabelConstants.PROPERTY_COLLECTION + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.PROPERTY_COLLECTION
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllPropertyCollectionResponseModel.LIST, list);
    responseMap.put(IGetAllPropertyCollectionResponseModel.COUNT, count);
    return responseMap;
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
    List<Map<String, Object>> pcToReturn = new ArrayList<>();
    for (Vertex propertyCollectionNode : searchResults) {
      Map<String, Object> propertyCollectionMap = UtilClass.getMapFromVertex(fieldsToFetch,
          propertyCollectionNode);
      pcToReturn.add(propertyCollectionMap);
    }
    return pcToReturn;
  }
}
