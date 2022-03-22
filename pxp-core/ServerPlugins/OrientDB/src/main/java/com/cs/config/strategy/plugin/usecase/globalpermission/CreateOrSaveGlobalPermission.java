package com.cs.config.strategy.plugin.usecase.globalpermission;

import java.util.Map;

import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class CreateOrSaveGlobalPermission extends AbstractCreateOrSaveGlobalPermission {
  
  public CreateOrSaveGlobalPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateOrSaveGlobalPermission/*" };
  }

  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    return saveOrCreateGlobalPermission(requestMap);
  }
  
}
