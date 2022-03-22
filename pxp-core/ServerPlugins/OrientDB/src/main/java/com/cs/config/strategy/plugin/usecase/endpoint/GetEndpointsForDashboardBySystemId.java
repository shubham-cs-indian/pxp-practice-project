package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.core.config.interactor.exception.endpoint.EndpointNotFoundException;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetEndpointsForDashboardBySystemId extends AbstractGetEndpointsForDashboard {
  
  public GetEndpointsForDashboardBySystemId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpointsForDashboardBySystemId/*" };
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
}
