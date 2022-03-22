package com.cs.core.config.target;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface IBulkCreateTargetsService
    extends ICreateConfigService<IListModel<ITargetModel>, IPluginSummaryModel> {
  
}
