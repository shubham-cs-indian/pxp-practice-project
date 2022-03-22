package com.cs.core.config.interactor.model.processdetails;

import java.util.Map;

public class SourceDestinationResponseModel implements ISourceDestinationResponseModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, String> sourceDestination;
  
  public Map<String, String> getSourceDestination()
  {
    return sourceDestination;
  }
  
  public void setSourceDestination(Map<String, String> sourceDestination)
  {
    this.sourceDestination = sourceDestination;
  }
}
