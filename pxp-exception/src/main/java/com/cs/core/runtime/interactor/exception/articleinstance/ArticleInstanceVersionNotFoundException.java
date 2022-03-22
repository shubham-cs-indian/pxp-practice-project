package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.configuration.VersionNotFoundException;

public class ArticleInstanceVersionNotFoundException extends VersionNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceVersionNotFoundException()
  {
    super();
  }
  
  public ArticleInstanceVersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
