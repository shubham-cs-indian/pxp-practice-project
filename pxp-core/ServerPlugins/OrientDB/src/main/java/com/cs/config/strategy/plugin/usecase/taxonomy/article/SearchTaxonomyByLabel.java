package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SearchTaxonomyByLabel extends AbstractSearchTaxonomyByLabel {
  
  public SearchTaxonomyByLabel(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SearchTaxonomyByLabel/*" };
  }
  
  @Override
  public String getVertexType()
  {
    return VertexLabelConstants.ROOT_KLASS_TAXONOMY;
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ROOT_KLASS_TAXONOMY;
  }
}
