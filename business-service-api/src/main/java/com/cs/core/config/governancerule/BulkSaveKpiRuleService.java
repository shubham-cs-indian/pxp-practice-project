package com.cs.core.config.governancerule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleResponseModel;
import com.cs.core.config.strategy.usecase.governancerule.IBulkSaveKpiRuleStrategy;

@Service
public class BulkSaveKpiRuleService extends AbstractSaveConfigService<IListModel<IBulkSaveKpiRuleRequestModel>, IBulkSaveKpiRuleResponseModel>
    implements IBulkSaveKpiRuleService {
  
  @Autowired
  protected IBulkSaveKpiRuleStrategy bulkSaveKpiRuleStrategy;
  
  @Autowired
  protected KpiValidations           kpiValidations;
  
  @Override
  public IBulkSaveKpiRuleResponseModel executeInternal(IListModel<IBulkSaveKpiRuleRequestModel> ruleModel)
      throws Exception
  {
    kpiValidations.validate(ruleModel);
    
    return bulkSaveKpiRuleStrategy.execute(ruleModel);
  }
}
