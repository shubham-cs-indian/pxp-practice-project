package com.cs.config.strategy.plugin.usecase.endpoint;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.core.config.interactor.exception.endpoint.EndpointNotFoundException;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetEndpointsForDashboardByUserId extends AbstractGetEndpointsForDashboard {
  
  public GetEndpointsForDashboardByUserId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpointsForDashboardByUserId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    try {
      return getEndpointsByDashboardIdOrSystemId(requestMap);
    }
    catch (Exception e) {
      throw new EndpointNotFoundException();
    }
  }
  
  @Override
  protected void fillFunctionPermission(Vertex roleNode, Map<String, Object> returnMap)
      throws Exception
  {
    Map<String, Boolean> functionPermission = GlobalPermissionUtils.getFunctionPermission(roleNode);
    returnMap.put(IGetEndointsInfoModel.FUNCTION_PERMISSION, functionPermission);
  }
}
