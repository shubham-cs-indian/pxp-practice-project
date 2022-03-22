package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleResponseModel;

public interface IBulkSaveKpiRule extends
    ISaveConfigInteractor<IListModel<IBulkSaveKpiRuleRequestModel>, IBulkSaveKpiRuleResponseModel> {
  
}
