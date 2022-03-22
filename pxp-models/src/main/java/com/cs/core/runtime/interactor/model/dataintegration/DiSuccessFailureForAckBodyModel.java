package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiSuccessFailureForAckBodyModel implements IDiSuccessFailureForAckBodyModel {
  
  private static final long        serialVersionUID = 1L;
  public List<String>              success          = new ArrayList<>();
  public List<Map<String, String>> failure          = new ArrayList<>();
  
  @Override
  public List<String> getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(List<String> success)
  {
    this.success = success;
  }
  
  @Override
  public List<Map<String, String>> getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(List<Map<String, String>> failure)
  {
    this.failure = failure;
  }
}
