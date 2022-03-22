package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IBulkSaveGoldenRecordRuleService
    extends ISaveConfigService<IListModel<IIdLabelCodeModel>, IBulkSaveGoldenRecordRuleResponseModel> {
  
}
