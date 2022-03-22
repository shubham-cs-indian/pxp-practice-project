package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetAssociatedAssetInstancesModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetAssociatedAssetInstancesStrategy
    extends IRuntimeStrategy<IIdParameterModel, IGetAssociatedAssetInstancesModel> {
  
}
