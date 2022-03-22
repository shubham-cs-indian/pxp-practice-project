package com.cs.core.config.interactor.model.condition;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IConditionInformationModel extends IConfigModel {
  
  public String getLabel();
  
  public void setLabel(String label);
}
