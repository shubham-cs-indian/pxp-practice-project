package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkSaveKpiRuleSuccessModel extends IModel {
  
  public static final String KPI_RULE_LIST  = "kpiRuleList";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public List<IBulkSaveKpiRuleModel> getKpiRuleList();
  
  public void setKpiRuleList(List<IBulkSaveKpiRuleModel> kpiRuleList);
  
  public IConfigDetailsForGridKpiRulesModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridKpiRulesModel kpiRuleList);
}
