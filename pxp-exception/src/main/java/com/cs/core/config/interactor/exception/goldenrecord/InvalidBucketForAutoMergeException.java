package com.cs.core.config.interactor.exception.goldenrecord;

import com.cs.core.exception.NotFoundException;

public class InvalidBucketForAutoMergeException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidBucketForAutoMergeException()
  {
    super();
  }
  
  public InvalidBucketForAutoMergeException(String message)
  {
    super(message);
  }
}
