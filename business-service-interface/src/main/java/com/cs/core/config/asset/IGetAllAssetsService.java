package com.cs.core.config.asset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;

public interface IGetAllAssetsService
    extends IGetConfigService<IAssetModel, IListModel<IKlassInformationModel>> {
  
}
