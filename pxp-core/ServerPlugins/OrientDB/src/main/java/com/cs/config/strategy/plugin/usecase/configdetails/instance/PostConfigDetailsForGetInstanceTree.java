package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.base.configdetails.instance.AbstractGetPostConfigDetailsForInstanceTree;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class PostConfigDetailsForGetInstanceTree
    extends AbstractGetPostConfigDetailsForInstanceTree {
  
  public PostConfigDetailsForGetInstanceTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|PostConfigDetailsForGetInstanceTree/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.executeInternal(requestMap);
  }
}
