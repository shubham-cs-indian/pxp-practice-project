package com.cs.config.strategy.plugin.usecase.ssoconfiguration;

import com.cs.config.strategy.plugin.usecase.base.ssoconfiguration.AbstractCreateSSOConfiguration;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class CreateSSOConfiguration extends AbstractCreateSSOConfiguration {
  
  public CreateSSOConfiguration(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Object execute(Map<String, Object> map) throws Exception
  {
    return create(map, VertexLabelConstants.SSO_CONFIGURATION);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateSSOConfiguration/*" };
  }
}
