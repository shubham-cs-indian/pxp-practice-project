package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleResponseModel;

public interface IBulkSaveKpiRuleService extends ISaveConfigService<IListModel<IBulkSaveKpiRuleRequestModel>, IBulkSaveKpiRuleResponseModel> {
  
}
