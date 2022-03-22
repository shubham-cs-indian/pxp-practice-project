package com.cs.core.config.asset;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkSaveAssetsService
    extends ISaveConfigService<IListModel<IAssetKlassSaveModel>, IPluginSummaryModel> {
  
}
