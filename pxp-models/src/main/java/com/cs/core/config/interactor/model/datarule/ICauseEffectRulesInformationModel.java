package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ICauseEffectRulesInformationModel extends IConfigModel {
  
  public String getLabel();
  
  public void setLabel(String label);
}
