package com.cs.core.runtime.interactor.usecase.staticcollection;

import com.cs.core.runtime.interactor.model.instancetree.IGetCollectionInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetCollectionInstanceTree extends 
IRuntimeInteractor<IGetCollectionInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel> {
}
