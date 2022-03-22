package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveKpiRuleStrategy extends
    IConfigStrategy<IListModel<IBulkSaveKpiRuleRequestModel>, IBulkSaveKpiRuleResponseModel> {
  
}
