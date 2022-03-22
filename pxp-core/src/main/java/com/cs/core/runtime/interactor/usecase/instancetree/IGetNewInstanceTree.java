package com.cs.core.runtime.interactor.usecase.instancetree;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetNewInstanceTree extends 
  IRuntimeInteractor<IGetInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel> {
}
