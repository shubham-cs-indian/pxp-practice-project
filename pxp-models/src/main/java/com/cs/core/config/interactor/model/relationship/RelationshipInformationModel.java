package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class RelationshipInformationModel extends ConfigEntityInformationModel
    implements IRelationshipInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          tabId;
  protected Boolean         isStandard;
  protected Boolean         isLite           = false;
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public Boolean getIsLite()
  {
    return isLite;
  }

  @Override
  public void setIsLite(Boolean isLite)
  {
    this.isLite = isLite;
  }
}
