package com.cs.core.config.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForGridKpiRulesModel extends IModel {
  
  public static final String REFERENCED_DASHBOARD_TABS = "referencedDashboardTabs";
  
  public Map<String, IIdLabelCodeModel> getReferencedDashboardTabs();
  
  public void setReferencedDashboardTabs(Map<String, IIdLabelCodeModel> referencedDashboardTabs);
}
