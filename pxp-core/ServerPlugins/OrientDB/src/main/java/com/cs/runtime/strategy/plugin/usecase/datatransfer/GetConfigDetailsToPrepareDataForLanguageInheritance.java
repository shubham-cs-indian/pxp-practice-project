package com.cs.runtime.strategy.plugin.usecase.datatransfer;

import com.cs.runtime.strategy.plugin.usecase.base.datatransfer.AbstractGetConfigDetailsToPrepareDataForLanguageInheritance;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetConfigDetailsToPrepareDataForLanguageInheritance
    extends AbstractGetConfigDetailsToPrepareDataForLanguageInheritance {
  
  public GetConfigDetailsToPrepareDataForLanguageInheritance(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsToPrepareDataForLanguageInheritance/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
}
