package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface IGetAllMasterAssets
    extends IGetConfigInteractor<IAssetModel, IListModel<IAssetModel>> {
  
}
