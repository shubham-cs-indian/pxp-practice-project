package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.configuration.TypeChangedException;

public class ArticleInstanceTypeChangedException extends TypeChangedException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceTypeChangedException()
  {
    super();
  }
  
  public ArticleInstanceTypeChangedException(TypeChangedException e)
  {
    super(e);
  }
}
