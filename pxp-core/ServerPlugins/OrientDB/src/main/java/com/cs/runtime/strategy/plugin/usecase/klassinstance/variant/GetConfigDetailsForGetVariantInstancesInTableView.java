package com.cs.runtime.strategy.plugin.usecase.klassinstance.variant;

import com.cs.runtime.strategy.plugin.usecase.base.variant.AbstractGetConfigDetailsForGetVariantInstancesInTableView;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetVariantInstancesInTableView
    extends AbstractGetConfigDetailsForGetVariantInstancesInTableView {
  
  public GetConfigDetailsForGetVariantInstancesInTableView(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetVariantInstancesInTableView/*" };
  }
}
