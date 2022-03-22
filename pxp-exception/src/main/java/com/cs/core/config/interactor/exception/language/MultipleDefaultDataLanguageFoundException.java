package com.cs.core.config.interactor.exception.language;

import com.cs.core.runtime.interactor.exception.configuration.MultipleVertexFoundException;

public class MultipleDefaultDataLanguageFoundException extends MultipleVertexFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MultipleDefaultDataLanguageFoundException()
  {
  }
  
  public MultipleDefaultDataLanguageFoundException(MultipleVertexFoundException e)
  {
    super(e);
  }
}
