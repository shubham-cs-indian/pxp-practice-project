package com.cs.config.strategy.plugin.usecase.dashboard;

import com.cs.config.strategy.plugin.usecase.configdetails.instance.AbstractGetConfigDetailsForInstanceTree;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetConfigDetailsForDashboardTileInformation
    extends AbstractGetConfigDetailsForInstanceTree {
  
  public GetConfigDetailsForDashboardTileInformation(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForDashboardTileInformation/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    Map<String, Object> mapToReturn = new HashMap<>();
    managePermissionDetailsForInstanceTree(userId, mapToReturn);
    return mapToReturn;
  }
}
