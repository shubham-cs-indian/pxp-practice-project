package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IGovernanceRule;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.runtime.interactor.model.configuration.IAdditionalPropertiesModel;

public interface IGovernanceRuleModel
    extends IConfigModel, IGovernanceRule, IAdditionalPropertiesModel {
  
  public static final String USER_ID        = "userId";
  public static final String CONFIG_DETAILS = "configDetails";
  public static final String KPI_ID         = "kpiId";
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public IConfigDetailsForGovernanceRuleModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGovernanceRuleModel configDetails);
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
}
