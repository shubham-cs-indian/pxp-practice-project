package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractCreateTagTaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.ParentKlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.exception.taxonomyarticle.ParentArticleTaxonomyNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class CreateMasterTaxonomy extends AbstractCreateTagTaxonomy {
  
  public CreateMasterTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateMasterTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.execute(requestMap);
    }
    catch (ParentKlassTaxonomyNotFoundException e) {
      throw new ParentArticleTaxonomyNotFoundException(e);
    }
  }
  
  public String getVertexType()
  {
    return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
  }
  
  @Override
  public String getTaxonomyLevelType()
  {
    return VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL;
  }
}
