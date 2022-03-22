package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiSuccessFailureCountModel extends IModel {
  
  public static final String SUCCESSCOUNT = "successCount";
  public static final String FAILURECOUNT = "failureCount";
  
  public String getSuccessCount();
  
  public void setSuccessCount(String successCount);
  
  public String getFailureCount();
  
  public void setFailureCount(String failureCount);
}
