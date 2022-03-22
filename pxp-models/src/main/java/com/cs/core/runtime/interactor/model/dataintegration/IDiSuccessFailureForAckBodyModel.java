package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IDiSuccessFailureForAckBodyModel extends IModel {
  
  public static final String SUCCESS = "success";
  public static final String FAILURE = "failure";
  
  public List<String> getSuccess();
  
  public void setSuccess(List<String> success);
  
  public List<Map<String, String>> getFailure();
  
  public void setFailure(List<Map<String, String>> failure);
}
