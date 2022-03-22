package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForFilterAndSortData;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGoldenRecordFilterAndSortData extends AbstractGetConfigDetailsForFilterAndSortData {
  
  public GetConfigDetailsForGoldenRecordFilterAndSortData(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForGoldenRecordFilterAndSortData/*"};
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    execute(requestMap, mapToReturn);
    return mapToReturn;
  }
}
