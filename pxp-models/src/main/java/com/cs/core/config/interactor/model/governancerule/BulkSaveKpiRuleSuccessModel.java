package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.model.attribute.IBulkSaveKpiRuleSuccessModel;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridKpiRulesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkSaveKpiRuleSuccessModel implements IBulkSaveKpiRuleSuccessModel {
  
  private static final long                    serialVersionUID = 1L;
  protected List<IBulkSaveKpiRuleModel>        kpiRuleList;
  protected IConfigDetailsForGridKpiRulesModel configDetails;
  
  @Override
  public List<IBulkSaveKpiRuleModel> getKpiRuleList()
  {
    return kpiRuleList;
  }
  
  @JsonDeserialize(contentAs = BulkSaveKpiRuleModel.class)
  @Override
  public void setKpiRuleList(List<IBulkSaveKpiRuleModel> kpiRuleList)
  {
    this.kpiRuleList = kpiRuleList;
  }
  
  @Override
  public IConfigDetailsForGridKpiRulesModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGridKpiRulesModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGridKpiRulesModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
