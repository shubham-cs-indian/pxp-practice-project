package com.cs.core.config.interactor.model.processevent;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBulkSaveProcessEventResponseModel extends IModel {
  
  public static final String FAILURE = "failure";
  public static final String SUCCESS = "success";
  
  public Map<String, Exception> getFailure();
  
  public void setFailure(Map<String, Exception> failure);
  
  public void setSuccess(IUploadProcessEventsResponseModel success);
  
  public IUploadProcessEventsResponseModel getSuccess();
  
}