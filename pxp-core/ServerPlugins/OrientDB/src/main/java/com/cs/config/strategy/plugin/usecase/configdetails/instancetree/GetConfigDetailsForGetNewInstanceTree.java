package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetNewInstanceTree;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetNewInstanceTree extends AbstractGetConfigDetailsForGetNewInstanceTree {

  public GetConfigDetailsForGetNewInstanceTree(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForGetNewInstanceTree/*"};
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    //ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    ConfigDetailsUtils.fillLinkedVariantPropertyCodes(mapToReturn);
    
    execute(requestMap, mapToReturn);
    
    String kpiId = (String) requestMap.get(IConfigDetailsForInstanceTreeGetRequestModel.KPI_ID);
    kpiHandling(kpiId, mapToReturn);
    
    return mapToReturn;
  }
}
