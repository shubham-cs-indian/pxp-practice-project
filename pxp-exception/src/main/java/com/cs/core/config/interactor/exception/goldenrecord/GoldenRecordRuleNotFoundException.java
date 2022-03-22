package com.cs.core.config.interactor.exception.goldenrecord;

import com.cs.core.exception.NotFoundException;

public class GoldenRecordRuleNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public GoldenRecordRuleNotFoundException()
  {
    super();
  }
  
  public GoldenRecordRuleNotFoundException(String message)
  {
    super(message);
  }
}
