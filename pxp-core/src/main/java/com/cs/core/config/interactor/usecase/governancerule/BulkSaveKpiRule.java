package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.core.config.governancerule.IBulkSaveKpiRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleResponseModel;
import com.cs.core.config.strategy.usecase.governancerule.IBulkSaveKpiRuleStrategy;

@Service
public class BulkSaveKpiRule extends AbstractSaveConfigInteractor<IListModel<IBulkSaveKpiRuleRequestModel>, IBulkSaveKpiRuleResponseModel>
    implements IBulkSaveKpiRule {
  
  @Autowired
  protected IBulkSaveKpiRuleService bulkSaveKpiRuleService;
  
  @Override
  public IBulkSaveKpiRuleResponseModel executeInternal(IListModel<IBulkSaveKpiRuleRequestModel> ruleModel)
      throws Exception
  {
    return bulkSaveKpiRuleService.execute(ruleModel);
  }
}
