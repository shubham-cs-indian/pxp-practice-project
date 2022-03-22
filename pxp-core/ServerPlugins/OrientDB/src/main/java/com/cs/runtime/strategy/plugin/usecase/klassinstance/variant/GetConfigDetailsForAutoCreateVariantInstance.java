package com.cs.runtime.strategy.plugin.usecase.klassinstance.variant;

import com.cs.runtime.strategy.plugin.usecase.base.variant.AbstractGetConfigDetailsForAutoCreateVariantInstance;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForAutoCreateVariantInstance
    extends AbstractGetConfigDetailsForAutoCreateVariantInstance {
  
  public GetConfigDetailsForAutoCreateVariantInstance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForAutoCreateVariantInstance/*" };
  }
}
