package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ParentNotFoundException;

public class ArticleInstanceParentNotFoundException extends ParentNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceParentNotFoundException()
  {
    super();
  }
  
  public ArticleInstanceParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
