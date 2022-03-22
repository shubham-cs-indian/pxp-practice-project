package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceVariantNotFoundException;

public class ArticleInstanceVariantNotFoundException extends InstanceVariantNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceVariantNotFoundException()
  {
    super();
  }
  
  public ArticleInstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
