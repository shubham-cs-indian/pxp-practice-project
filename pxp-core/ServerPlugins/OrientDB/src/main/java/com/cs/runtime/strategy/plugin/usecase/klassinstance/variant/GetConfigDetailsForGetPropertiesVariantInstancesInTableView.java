package com.cs.runtime.strategy.plugin.usecase.klassinstance.variant;

import com.cs.runtime.strategy.plugin.usecase.base.variant.AbstractGetConfigDetailsForGetPropertiesVariantInstancesInTableView;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetPropertiesVariantInstancesInTableView
    extends AbstractGetConfigDetailsForGetPropertiesVariantInstancesInTableView {
  
  public GetConfigDetailsForGetPropertiesVariantInstancesInTableView(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetPropertiesVariantInstancesInTableView/*" };
  }
}
