package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractGetChildHierarchyTaxonomiesForTaxonomyLevel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetChildHierarchyTaxonomiesForTaxonomyLevel
    extends AbstractGetChildHierarchyTaxonomiesForTaxonomyLevel {
  
  public GetChildHierarchyTaxonomiesForTaxonomyLevel(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetChildHierarchyTaxonomiesForTaxonomyLevel/*" };
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
