package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IBulkSaveKpiRuleRequestModel extends IIdLabelCodeModel {
  
  public static final String ADDED_DASHBOARD_TAB_ID   = "addedDashboardTabId";
  public static final String DELETED_DASHBOARD_TAB_ID = "deletedDashboardTabId";
  
  public String getAddedDashboardTabId();
  
  public void setAddedDashboardTabId(String addedDashboardTabId);
  
  public String getDeletedDashboardTabId();
  
  public void setDeletedDashboardTabId(String deletedDashboardTabId);
}
