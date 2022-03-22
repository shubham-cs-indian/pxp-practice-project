package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDashboardDataModel extends IModel {
  
  public String getExternalData();
  
  public void setExternalData(String externalData);
}
