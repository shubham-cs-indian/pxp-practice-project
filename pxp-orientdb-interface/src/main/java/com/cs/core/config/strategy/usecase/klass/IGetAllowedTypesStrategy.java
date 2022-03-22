package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesModel;

public interface IGetAllowedTypesStrategy
    extends IConfigStrategy<IGetAllowedTypesModel, IListModel<String>> {
  
}
