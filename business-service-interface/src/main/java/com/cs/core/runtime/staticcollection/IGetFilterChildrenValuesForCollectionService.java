package com.cs.core.runtime.staticcollection;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;

public interface IGetFilterChildrenValuesForCollectionService extends
    IRuntimeService<IGetFilterChildrenForCollectionRequestModel, IGetFilterChildrenResponseModel> {
  
}