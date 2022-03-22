package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetMultiClassificationKlassDetails;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetMultiClassificationTargetDetails
    extends AbstractGetMultiClassificationKlassDetails {
  
  public GetMultiClassificationTargetDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMultiClassificationTargetDetails/*" };
  }
  
  @Override
  protected String getKlassNodeLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_TARGET;
  }
}
