package com.cs.config.strategy.plugin.usecase.grideditablepropertylist;

import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertiesResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllGridEditableProperties extends AbstractOrientPlugin {
  
  public GetAllGridEditableProperties(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllGridEditableProperties/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    List<String> idsToExclude = (List<String>) requestMap
        .get(IConfigGetAllRequestModel.IDS_TO_EXCLUDE);
    
    List<Map<String, Object>> propertyList = GridEditUtil.getAllGridEditProperties(from, size,
        searchText, searchColumn, sortBy, sortOrder, idsToExclude);
    Long totalCount = GridEditUtil.getTotalCount(idsToExclude);
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetGridEditPropertiesResponseModel.GRID_EDIT_PROPERTIES, propertyList);
    returnMap.put(IGetGridEditPropertiesResponseModel.TOTAL_COUNT, totalCount);
    
    return returnMap;
  }
}
