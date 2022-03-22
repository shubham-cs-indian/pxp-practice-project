package com.cs.core.runtime.staticcollection;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetCollectionInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;

public interface IGetCollectionInstanceTreeService
    extends IRuntimeService<IGetCollectionInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel> {
}
