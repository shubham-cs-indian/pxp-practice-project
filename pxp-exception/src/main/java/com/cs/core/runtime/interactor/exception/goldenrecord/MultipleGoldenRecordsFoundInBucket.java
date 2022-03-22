package com.cs.core.runtime.interactor.exception.goldenrecord;

import com.cs.core.exception.PluginException;

public class MultipleGoldenRecordsFoundInBucket extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public MultipleGoldenRecordsFoundInBucket()
  {
  }
  
  public MultipleGoldenRecordsFoundInBucket(PluginException e)
  {
    super(e);
  }
  
  public MultipleGoldenRecordsFoundInBucket(String message)
  {
    super(message);
  }
}
