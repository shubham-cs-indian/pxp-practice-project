package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkSaveAssets
    extends ISaveConfigInteractor<IListModel<IAssetKlassSaveModel>, IPluginSummaryModel> {
  
}
