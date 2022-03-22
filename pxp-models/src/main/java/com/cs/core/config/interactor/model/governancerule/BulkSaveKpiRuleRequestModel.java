package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;

public class BulkSaveKpiRuleRequestModel extends IdLabelCodeModel
    implements IBulkSaveKpiRuleRequestModel {
  
  protected String addedDashboardTabId;
  protected String deletedDashboardTabId;
  
  @Override
  public String getAddedDashboardTabId()
  {
    return addedDashboardTabId;
  }
  
  @Override
  public void setAddedDashboardTabId(String addedDashboardTabId)
  {
    this.addedDashboardTabId = addedDashboardTabId;
  }
  
  @Override
  public String getDeletedDashboardTabId()
  {
    return deletedDashboardTabId;
  }
  
  @Override
  public void setDeletedDashboardTabId(String deletedDashboardTabId)
  {
    this.deletedDashboardTabId = deletedDashboardTabId;
  }
}
