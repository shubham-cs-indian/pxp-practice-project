package com.cs.config.strategy.plugin.usecase.grideditablepropertylist;

import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetAllGridEditablePropertyListWithAvaliableSequence extends AbstractOrientPlugin {
  
  public GetAllGridEditablePropertyListWithAvaliableSequence(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllGridEditablePropertyListWithAvaliableSequence/*" };
  }
  
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
    Boolean isRuntimeRequest = (Boolean) requestMap
        .get(IGetGridEditPropertyListRequestModel.IS_RUNTIME_REQUEST);
    
    Map<String, Object> returnMap = GridEditUtil.fetchGridEditData(from, size, searchText,
        searchColumn, sortBy, sortOrder, isRuntimeRequest);
    
    return returnMap;
  }
}
