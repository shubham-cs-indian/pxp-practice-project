package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IBulkSaveGoldenRecordRuleStrategy
    extends IConfigStrategy<IListModel<IIdLabelCodeModel>, IBulkSaveGoldenRecordRuleResponseModel> {
  
}
