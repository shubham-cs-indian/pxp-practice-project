package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateAssetStrategy
    extends IConfigStrategy<IListModel<IAssetModel>, IAssetModel> {
  
}
