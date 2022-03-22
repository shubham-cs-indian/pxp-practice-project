package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractGetKlassAndAttributionTaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.exception.taxonomyarticle.ArticleTaxonomyNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetKlassAndAttributionTaxonomy extends AbstractGetKlassAndAttributionTaxonomy {
  
  public GetKlassAndAttributionTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassAndAttributionTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.execute(requestMap);
    }
    catch (KlassTaxonomyNotFoundException e) {
      throw new ArticleTaxonomyNotFoundException(e);
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
