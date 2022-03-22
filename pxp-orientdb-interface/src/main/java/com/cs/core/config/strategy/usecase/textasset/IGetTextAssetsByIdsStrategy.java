package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetTextAssetsByIdsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IKlass>> {
  
}
