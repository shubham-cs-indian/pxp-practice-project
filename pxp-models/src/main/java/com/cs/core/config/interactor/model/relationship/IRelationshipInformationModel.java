package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IRelationshipInformationModel extends IConfigEntityInformationModel {
  
  String TAB_ID      = "tabId";
  String IS_STANDARD = "isStandard";
  String IS_LITE     = "isLite";
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public Boolean getIsLite();
  
  public void setIsLite(Boolean isLite);
}
