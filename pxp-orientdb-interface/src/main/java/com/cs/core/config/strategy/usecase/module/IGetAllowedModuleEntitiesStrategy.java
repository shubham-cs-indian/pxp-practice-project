package com.cs.core.config.strategy.usecase.module;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllowedModuleEntitiesStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<String>> {
  
}
