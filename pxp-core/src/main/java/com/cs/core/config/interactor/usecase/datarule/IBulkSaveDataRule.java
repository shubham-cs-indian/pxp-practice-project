package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;

public interface IBulkSaveDataRule
    extends ISaveConfigInteractor<IListModel<IBulkSaveDataRuleModel>, IBulkSaveDataRuleResponseModel> {
  
}
