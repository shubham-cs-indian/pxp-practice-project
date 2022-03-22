package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IJmsImportComponentResponseModel extends IModel {
  
  public static final String ACTUAL_INSTANCE_IMPORTED = "actualInstancesImported";
  public static final String FAILURE_MESSAGE          = "failureMessage";
  
  public Long getActualInstancesImported();
  
  public void setActualInstancesImported(Long actualInstancesImported);
  
  public String getFailureMessage();
  
  public void setFailureMessage(String failureMessage);
}
