package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;

public class BulkSaveKpiRuleModel extends IdLabelCodeModel implements IBulkSaveKpiRuleModel {
  
  private static final long serialVersionUID = 1L;
  protected String          dashboardTabId;
  
  public String getDashboardTabId()
  {
    return dashboardTabId;
  }
  
  public void setDashboardTabId(String dashboardTabId)
  {
    this.dashboardTabId = dashboardTabId;
  }
}
