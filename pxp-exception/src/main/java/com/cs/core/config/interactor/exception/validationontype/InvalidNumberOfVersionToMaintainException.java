package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.PluginException;

public class InvalidNumberOfVersionToMaintainException extends PluginException {

  private static final long serialVersionUID = 1L;

  public InvalidNumberOfVersionToMaintainException()
  {
    super();
  }

  public InvalidNumberOfVersionToMaintainException(InvalidTypeException e)
  {
    super(e);
  }
}
