package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IBulkSaveKpiRuleModel extends IIdLabelCodeModel {
  
  public static final String DASHBOARD_TAB_ID = "dashboardTabId";
  
  public String getDashboardTabId();
  
  public void setDashboardTabId(String DashboardTabId);
}
