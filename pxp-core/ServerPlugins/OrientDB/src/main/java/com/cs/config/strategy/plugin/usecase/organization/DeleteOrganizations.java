package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.strategy.plugin.usecase.base.organization.AbstractDeleteOrganizations;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class DeleteOrganizations extends AbstractDeleteOrganizations {
  
  public DeleteOrganizations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteOrganizations/*" };
  }
}
