package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveDataRuleStrategy
    extends IConfigStrategy<IListModel<IBulkSaveDataRuleModel>, IBulkSaveDataRuleResponseModel> {
  
}
