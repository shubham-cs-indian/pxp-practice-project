package com.cs.core.config.strategy.usecase.instancetree;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;

public interface IGetDefaultTypesStrategy
    extends IConfigStrategy<IDefaultTypesRequestModel, IListModel<IGetDefaultTypesResponseModel>> {
  
}
