package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAddedPropertiesDiffStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IDefaultValueChangeModel>> {
  
}
