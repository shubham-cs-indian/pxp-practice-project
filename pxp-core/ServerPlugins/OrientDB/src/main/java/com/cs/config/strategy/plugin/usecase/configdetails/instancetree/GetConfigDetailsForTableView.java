package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetNewInstanceTree;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

import java.util.HashMap;
import java.util.Map;

public class GetConfigDetailsForTableView extends AbstractGetConfigDetailsForGetNewInstanceTree {

  public GetConfigDetailsForTableView(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    execute(requestMap, mapToReturn);
    return mapToReturn;
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForTableView/*"};
  }
}
