package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.strategy.plugin.usecase.base.organization.AbstractGetAllOrganization;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetAllOrganizations extends AbstractGetAllOrganization {
  
  public GetAllOrganizations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllOrganizations/*" };
  }
}
