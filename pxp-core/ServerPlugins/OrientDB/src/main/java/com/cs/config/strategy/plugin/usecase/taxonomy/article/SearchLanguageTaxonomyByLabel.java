package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SearchLanguageTaxonomyByLabel extends AbstractSearchTaxonomyByLabel {
  
  public SearchLanguageTaxonomyByLabel(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SearchLanguageTaxonomyByLabel/*" };
  }
  
  @Override
  public String getVertexType()
  {
    return VertexLabelConstants.LANGUAGE;
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.LANGUAGE;
  }
}
