package com.cs.core.runtime.interactor.exception.talend;

public class TalendInputException extends TalendException {
  
  private static final long serialVersionUID = 1L;
  
  public TalendInputException()
  {
    super();
  }
  
  public TalendInputException(String message)
  {
    super(message);
  }
  
  public TalendInputException(TalendInputException exception)
  {
    super(exception);
  }
}
