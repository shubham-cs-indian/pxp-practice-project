package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class ArticleInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceNotFoundException()
  {
    super();
  }
  
  public ArticleInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
