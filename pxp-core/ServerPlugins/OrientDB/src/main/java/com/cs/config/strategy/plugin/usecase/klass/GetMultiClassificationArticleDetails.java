package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetMultiClassificationKlassDetails;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetMultiClassificationArticleDetails
    extends AbstractGetMultiClassificationKlassDetails {
  
  public GetMultiClassificationArticleDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMultiClassificationArticleDetails/*" };
  }
  
  @Override
  protected String getKlassNodeLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_KLASS;
  }
}
