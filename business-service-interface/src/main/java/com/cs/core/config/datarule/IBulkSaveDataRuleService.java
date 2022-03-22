package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;

public interface IBulkSaveDataRuleService extends ISaveConfigService<IListModel<IBulkSaveDataRuleModel>, IBulkSaveDataRuleResponseModel> {
  
}
