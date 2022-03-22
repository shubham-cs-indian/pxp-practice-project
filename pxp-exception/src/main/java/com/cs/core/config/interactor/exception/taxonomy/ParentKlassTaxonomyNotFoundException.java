package com.cs.core.config.interactor.exception.taxonomy;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ParentNotFoundException;

public class ParentKlassTaxonomyNotFoundException extends ParentNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentKlassTaxonomyNotFoundException()
  {
  }
  
  public ParentKlassTaxonomyNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
