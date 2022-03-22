package com.cs.core.config.interactor.usecase.target;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface IBulkCreateTargets
    extends ICreateConfigInteractor<IListModel<ITargetModel>, IPluginSummaryModel> {
  
}
