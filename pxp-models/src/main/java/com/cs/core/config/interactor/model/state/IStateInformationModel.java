package com.cs.core.config.interactor.model.state;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IStateInformationModel extends IConfigModel {
  
  public String getLabel();
  
  public void setLabel(String label);
}
