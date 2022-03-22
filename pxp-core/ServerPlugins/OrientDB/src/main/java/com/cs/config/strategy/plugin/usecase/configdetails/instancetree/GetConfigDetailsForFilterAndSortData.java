package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForFilterAndSortData;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForFilterAndSortData extends AbstractGetConfigDetailsForFilterAndSortData {
  
  public GetConfigDetailsForFilterAndSortData(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForFilterAndSortData/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();

    execute(requestMap, mapToReturn);
    
    String kpiId = (String) requestMap.get(IConfigDetailsForFilterAndSortInfoRequestModel.KPI_ID);
    kpiHandling(kpiId, mapToReturn);
    
    return mapToReturn;
  }
}
