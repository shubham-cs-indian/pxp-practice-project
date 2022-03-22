package com.cs.core.runtime.interactor.model.purge;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IPurgeResponseModel extends IModel {
  
  public static final String SUCCESS_IDS = "successIds";
  public static final String FAILURE_IDS = "failureIds";
  
  public List<String> getSuccessIds();
  
  public void setSuccessIds(List<String> successIds);
  
  public List<String> getFailureIds();
  
  public void setFailureIds(List<String> failureIds);
}
