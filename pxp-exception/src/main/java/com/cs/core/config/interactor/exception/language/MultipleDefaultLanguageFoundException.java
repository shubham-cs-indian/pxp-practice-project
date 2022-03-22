package com.cs.core.config.interactor.exception.language;

import com.cs.core.runtime.interactor.exception.configuration.MultipleVertexFoundException;

public class MultipleDefaultLanguageFoundException extends MultipleVertexFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MultipleDefaultLanguageFoundException()
  {
  }
  
  public MultipleDefaultLanguageFoundException(MultipleVertexFoundException e)
  {
    super(e);
  }
}
