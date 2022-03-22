package com.cs.core.runtime.interactor.exception.goldenrecord;

import com.cs.core.exception.PluginException;

public class SourceRecordsUpdatedForBucketException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public SourceRecordsUpdatedForBucketException()
  {
    super();
  }
  
  public SourceRecordsUpdatedForBucketException(String message)
  {
    super(message);
  }
}
