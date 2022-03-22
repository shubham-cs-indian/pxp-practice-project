package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractDeleteTagTaxonomy;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class DeleteMasterTaxonomy extends AbstractDeleteTagTaxonomy {
  
  public DeleteMasterTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteMasterTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  public String getVertexType()
  {
    return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
  }
}
