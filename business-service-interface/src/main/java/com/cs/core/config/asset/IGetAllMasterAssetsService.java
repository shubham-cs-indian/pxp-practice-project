package com.cs.core.config.asset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface IGetAllMasterAssetsService
    extends IGetConfigService<IAssetModel, IListModel<IAssetModel>> {
  
}
