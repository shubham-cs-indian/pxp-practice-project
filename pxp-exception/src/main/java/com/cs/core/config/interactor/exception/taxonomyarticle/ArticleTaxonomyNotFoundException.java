package com.cs.core.config.interactor.exception.taxonomyarticle;

import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;

public class ArticleTaxonomyNotFoundException extends KlassTaxonomyNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleTaxonomyNotFoundException()
  {
  }
  
  public ArticleTaxonomyNotFoundException(KlassTaxonomyNotFoundException e)
  {
    super(e);
  }
}
