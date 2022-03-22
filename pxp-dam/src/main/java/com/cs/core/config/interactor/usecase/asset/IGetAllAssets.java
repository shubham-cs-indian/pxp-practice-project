package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;

public interface IGetAllAssets
    extends IGetConfigInteractor<IAssetModel, IListModel<IKlassInformationModel>> {
  
}
