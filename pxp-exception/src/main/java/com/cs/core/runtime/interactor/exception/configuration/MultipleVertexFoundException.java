package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class MultipleVertexFoundException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public MultipleVertexFoundException()
  {
  }
  
  public MultipleVertexFoundException(MultipleVertexFoundException e)
  {
    super(e);
  }
}
