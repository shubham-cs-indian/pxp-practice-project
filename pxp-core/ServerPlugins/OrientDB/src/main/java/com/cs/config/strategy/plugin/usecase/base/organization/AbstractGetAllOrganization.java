package com.cs.config.strategy.plugin.usecase.base.organization;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetAllOrganization extends AbstractOrientPlugin {
  
  public static final List<String> fieldToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IOrganizationModel.LABEL, IOrganizationModel.IS_STANDARD, IOrganization.CODE,
      IOrganizationModel.ICON);
  
  public AbstractGetAllOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
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
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    List<Map<String, Object>> returnList = new ArrayList<>();
    String query = "select from " + VertexLabelConstants.ORGANIZATION + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    String countQuery = "select count(*) from " + VertexLabelConstants.ORGANIZATION
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Iterable<Vertex> organizationVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex organizationVertex : organizationVertices) {
      Map<String, Object> organizationMap = UtilClass.getMapFromVertex(fieldToFetch,
          organizationVertex);
      returnList.add(organizationMap);
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetAllOrganizationResponseModel.LIST, returnList);
    returnMap.put(IGetAllOrganizationResponseModel.COUNT, count);
    return returnMap;
  }
}
