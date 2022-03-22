package com.cs.core.config.interactor.exception.language;

import com.cs.core.runtime.interactor.exception.configuration.MultipleVertexFoundException;

public class MultipleDefaultUILanguageFoundException extends MultipleVertexFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MultipleDefaultUILanguageFoundException()
  {
  }
  
  public MultipleDefaultUILanguageFoundException(MultipleVertexFoundException e)
  {
    super(e);
  }
}
