package com.cs.core.runtime.interactor.model.configdetails;

public interface IConfigDetailsForImportSwitchTypeRequestModel
    extends IConfigDetailsForSwitchTypeRequestModel {
  
  public String getComponentId();
  
  public void setComponentId(String isGetAll);
}
