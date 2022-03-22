package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetNewInstanceTree;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetGoldenRecordBucketInstances extends AbstractGetConfigDetailsForGetNewInstanceTree {

  public GetConfigDetailsForGetGoldenRecordBucketInstances(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForGetGoldenRecordBucketInstances/*"};
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    execute(requestMap, mapToReturn);
    return mapToReturn;
  }
  
}
