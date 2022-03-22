package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetMultiClassificationKlassDetails;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetMultiClassificationAssetDetails extends AbstractGetMultiClassificationKlassDetails {
  
  public GetMultiClassificationAssetDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMultiClassificationAssetDetails/*" };
  }
  
  @Override
  protected String getKlassNodeLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_ASSET;
  }
}
