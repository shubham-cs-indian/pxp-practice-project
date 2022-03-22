package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.strategy.plugin.usecase.base.organization.AbstractCreateOrganization;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class CreateOrganization extends AbstractCreateOrganization {
  
  public CreateOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateOrganization/*" };
  }
}
