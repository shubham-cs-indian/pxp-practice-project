package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetMultiClassificationKlassDetails;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetMultiClassificationTextAssetDetails
    extends AbstractGetMultiClassificationKlassDetails {
  
  public GetMultiClassificationTextAssetDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMultiClassificationTextAssetDetails/*" };
  }
  
  @Override
  protected String getKlassNodeLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
  }
}
