package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractGetAllowedTagValuesForTagTaxonomy;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetAllowedTagValuesForMasterTaxonomy
    extends AbstractGetAllowedTagValuesForTagTaxonomy {
  
  public GetAllowedTagValuesForMasterTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedTagValuesForMasterTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.execute(requestMap);
    }
    catch (Exception e) {
      throw e;
    }
  }
}
