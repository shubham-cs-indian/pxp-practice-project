package com.cs.core.config.interactor.model.processevent;

import java.util.Map;

public class BulkSaveProcessEventResponseModel implements IBulkSaveProcessEventResponseModel {
  
  private static final long                   serialVersionUID = 1L;
  protected IUploadProcessEventsResponseModel success;
  protected Map<String, Exception>            failure;
  
  @Override
  public Map<String, Exception> getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(Map<String, Exception> failure)
  {
    this.failure = failure;
  }
  
  @Override
  public void setSuccess(IUploadProcessEventsResponseModel success)
  {
    this.success = success;
    
  }
  
  @Override
  public IUploadProcessEventsResponseModel getSuccess()
  {
    return success;
  }
  
}