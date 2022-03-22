package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveAssetStrategy
    extends IConfigStrategy<IListModel<IAssetKlassSaveModel>, IPluginSummaryModel> {
  
}
