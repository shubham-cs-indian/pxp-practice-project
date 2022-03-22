package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.base.configdetails.instance.AbstractGetPostConfigDetailsForRelationships;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class PostConfigDetailsForRelationships
    extends AbstractGetPostConfigDetailsForRelationships {
  
  public PostConfigDetailsForRelationships(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|PostConfigDetailsForRelationships/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.executeInternal(requestMap);
  }
}
