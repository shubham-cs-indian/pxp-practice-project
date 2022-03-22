package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.strategy.plugin.usecase.base.organization.AbstractSaveOrganization;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SaveOrganization extends AbstractSaveOrganization {
  
  public SaveOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveOrganization/*" };
  }
}
