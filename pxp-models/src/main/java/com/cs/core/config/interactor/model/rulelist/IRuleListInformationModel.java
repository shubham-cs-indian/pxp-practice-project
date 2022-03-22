package com.cs.core.config.interactor.model.rulelist;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IRuleListInformationModel extends IConfigModel {
  
  public String getLabel();
  
  public void setLabel(String label);
}
