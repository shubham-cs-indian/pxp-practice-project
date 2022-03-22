package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkCreateAssets
    extends ICreateConfigInteractor<IListModel<IAssetModel>, IPluginSummaryModel> {
  
}
