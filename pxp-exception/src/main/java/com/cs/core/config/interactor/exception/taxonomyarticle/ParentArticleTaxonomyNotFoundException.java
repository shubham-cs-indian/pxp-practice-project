package com.cs.core.config.interactor.exception.taxonomyarticle;

import com.cs.core.config.interactor.exception.taxonomy.ParentKlassTaxonomyNotFoundException;

public class ParentArticleTaxonomyNotFoundException extends ParentKlassTaxonomyNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentArticleTaxonomyNotFoundException()
  {
  }
  
  public ParentArticleTaxonomyNotFoundException(ParentKlassTaxonomyNotFoundException e)
  {
    super(e);
  }
}
