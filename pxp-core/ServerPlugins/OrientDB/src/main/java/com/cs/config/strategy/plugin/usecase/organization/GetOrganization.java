package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.strategy.plugin.usecase.base.organization.AbstractGetOrganization;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetOrganization extends AbstractGetOrganization {
  
  public GetOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrganization/*" };
  }
}
