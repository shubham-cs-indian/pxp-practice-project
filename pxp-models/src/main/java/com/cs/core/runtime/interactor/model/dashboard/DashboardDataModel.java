package com.cs.core.runtime.interactor.model.dashboard;

public class DashboardDataModel implements IDashboardDataModel {
  
  String externalData;
  
  public String getExternalData()
  {
    return externalData;
  }
  
  public void setExternalData(String externalData)
  {
    this.externalData = externalData;
  }
}
