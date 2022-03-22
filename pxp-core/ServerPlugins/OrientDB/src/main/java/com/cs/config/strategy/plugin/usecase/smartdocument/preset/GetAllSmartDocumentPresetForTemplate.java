package com.cs.config.strategy.plugin.usecase.smartdocument.preset;

import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetAllSmartDocumentPresetForTemplate extends AbstractOrientPlugin {
  
  public GetAllSmartDocumentPresetForTemplate(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllSmartDocumentPresetForTemplate/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return null;
  }
}
