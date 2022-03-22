package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAssetsByIdsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IKlass>> {
  
}
