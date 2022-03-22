package com.cs.core.exception;

public class InvalidDateFormatException extends PluginException {

  private static final long serialVersionUID = 1L;

  public InvalidDateFormatException()
  {
    super();
  }

  public InvalidDateFormatException(Exception e)
  {
    super(e);
  }
}
