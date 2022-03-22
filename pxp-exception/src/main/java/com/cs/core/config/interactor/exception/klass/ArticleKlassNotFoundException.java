package com.cs.core.config.interactor.exception.klass;

import com.cs.core.exception.KlassNotFoundException;

public class ArticleKlassNotFoundException extends KlassNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleKlassNotFoundException()
  {
  }
  
  public ArticleKlassNotFoundException(KlassNotFoundException e)
  {
    super(e);
  }
}
