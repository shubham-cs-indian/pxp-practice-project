package com.cs.core.exception;

public class InvalidNumberFormatException extends PluginException {

  private static final long serialVersionUID = 1L;

  public InvalidNumberFormatException()
  {
    super();
  }

  public InvalidNumberFormatException(Exception e)
  {
    super(e);
  }
}
