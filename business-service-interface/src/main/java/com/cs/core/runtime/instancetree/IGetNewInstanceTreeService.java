package com.cs.core.runtime.instancetree;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;

public interface IGetNewInstanceTreeService extends IRuntimeService<IGetInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel> {
}
