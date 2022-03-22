package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetFilterChildren;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetFilterChildren extends AbstractGetConfigDetailsForGetFilterChildren {

  public GetConfigDetailsForGetFilterChildren(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForGetFilterChildren/*"};
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    execute(requestMap, mapToReturn);
    
    String kpiId = (String) requestMap.get(IConfigDetailsForGetFilterChildrenRequestModel.KPI_ID);
    kpiHandling(kpiId, mapToReturn);
    
    return mapToReturn;
  }
}
