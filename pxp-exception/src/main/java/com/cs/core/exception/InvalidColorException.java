package com.cs.core.exception;

public class InvalidColorException extends PluginException{
  private static final long serialVersionUID = 1L;

  public InvalidColorException()
  {
    super();
  }

  public InvalidColorException(Exception e)
  {
    super(e);
  }
}
