package com.cs.core.runtime.interactor.exception.talend;

import com.cs.core.exception.PluginException;

public class TalendException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public TalendException()
  {
    super();
  }
  
  public TalendException(String message)
  {
    super(message);
  }
  
  public TalendException(TalendException exception)
  {
    super(exception);
  }
}
