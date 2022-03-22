package com.cs.config.strategy.plugin.usecase.themeconfiguration;

import java.util.Map;

import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SaveDefaultThemeConfiguration extends SaveThemeConfiguration {
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }

  public SaveDefaultThemeConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveDefaultThemeConfiguration/*" };
  }
  
}
