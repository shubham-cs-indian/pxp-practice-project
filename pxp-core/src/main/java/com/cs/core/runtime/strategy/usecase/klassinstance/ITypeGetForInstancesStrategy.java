package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.ITypesGetForInstancesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetInstanceTypesResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ITypeGetForInstancesStrategy
    extends IRuntimeStrategy<ITypesGetForInstancesRequestModel, IGetInstanceTypesResponseModel> {
  
}
