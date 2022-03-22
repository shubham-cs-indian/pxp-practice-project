package com.cs.core.exception;

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
