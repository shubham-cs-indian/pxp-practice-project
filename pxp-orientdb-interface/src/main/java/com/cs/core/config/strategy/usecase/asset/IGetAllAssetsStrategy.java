package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllAssetsStrategy
    extends IConfigStrategy<IAssetModel, IListModel<IKlassInformationModel>> {
  
}
