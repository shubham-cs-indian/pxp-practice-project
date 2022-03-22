package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IBulkSaveGoldenRecordRule extends
    ISaveConfigInteractor<IListModel<IIdLabelCodeModel>, IBulkSaveGoldenRecordRuleResponseModel> {
  
}
