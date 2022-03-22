package com.cs.core.config.asset;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkCreateAssetsService
    extends ICreateConfigService<IListModel<IAssetModel>, IPluginSummaryModel> {
  
}
