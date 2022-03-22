package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IDataRuleInformationModel extends IConfigModel {
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
}
