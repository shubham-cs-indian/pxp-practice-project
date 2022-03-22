package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class ArticleInstanceAlreadyExistsException extends InstanceAlreadyExistsException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceAlreadyExistsException()
  {
    super();
  }
  
  public ArticleInstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
