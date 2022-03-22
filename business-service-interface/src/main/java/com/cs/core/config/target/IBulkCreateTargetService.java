package com.cs.core.config.target;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface IBulkCreateTargetService
    extends ICreateConfigService<IListModel<ITargetModel>, IPluginSummaryModel> {
  
}
