package com.cs.config.strategy.plugin.usecase.klass.supplier;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetMultiClassificationKlassDetails;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetMultiClassificationSupplierDetails
    extends AbstractGetMultiClassificationKlassDetails {
  
  public GetMultiClassificationSupplierDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMultiClassificationSupplierDetails/*" };
  }
  
  @Override
  protected String getKlassNodeLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
  }
}
