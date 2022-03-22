package com.cs.core.config.interactor.exception.taxonomy;

import com.cs.core.exception.NotFoundException;

public class KlassTaxonomyNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public KlassTaxonomyNotFoundException()
  {
  }
  
  public KlassTaxonomyNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
