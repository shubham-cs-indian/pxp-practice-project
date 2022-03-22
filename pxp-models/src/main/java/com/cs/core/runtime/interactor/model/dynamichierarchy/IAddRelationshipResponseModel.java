package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

import java.util.List;

public interface IAddRelationshipResponseModel extends IModel {
  
  public static final String FAILED_INSTANCES  = "failedInstances";
  public static final String SUCCESS_INSTANCES = "successInstances";
  
  public List<IIdAndNameModel> getSuccessInstances();
  
  public void setSuccessInstances(List<IIdAndNameModel> successInstances);
  
  public IExceptionModel getFailedInstances();
  
  public void setFailedInstances(IExceptionModel failedInstances);
}
