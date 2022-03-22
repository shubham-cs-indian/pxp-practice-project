package com.cs.config.strategy.plugin.usecase.repair;

import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class ConfigDummyMigration extends AbstractOrientMigration {
  
  public ConfigDummyMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ConfigDummyMigration/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    return null;
  }
}
