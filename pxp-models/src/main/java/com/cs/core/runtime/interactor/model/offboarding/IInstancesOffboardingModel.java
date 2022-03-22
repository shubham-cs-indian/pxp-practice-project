package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IInstancesOffboardingModel extends IIdsListParameterModel {
  
  public static final String MODULE = "module";
  
  public String getModule();
  
  public void setModule(String module);
}
