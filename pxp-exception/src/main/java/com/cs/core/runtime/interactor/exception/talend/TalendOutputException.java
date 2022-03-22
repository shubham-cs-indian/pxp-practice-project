package com.cs.core.runtime.interactor.exception.talend;

public class TalendOutputException extends TalendException {
  
  private static final long serialVersionUID = 1L;
  
  public TalendOutputException()
  {
    super();
  }
  
  public TalendOutputException(String message)
  {
    super(message);
  }
  
  public TalendOutputException(TalendOutputException exception)
  {
    super(exception);
  }
}
