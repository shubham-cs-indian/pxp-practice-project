package com.cs.core.runtime.interactor.exception.goldenrecord;

import com.cs.core.exception.NotFoundException;

public class GoldenRecordRuleBucketNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public GoldenRecordRuleBucketNotFoundException()
  {
    super();
  }
  
  public GoldenRecordRuleBucketNotFoundException(String message)
  {
    super(message);
  }
}
