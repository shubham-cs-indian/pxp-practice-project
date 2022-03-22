package com.cs.core.runtime.interactor.usecase.staticcollection;

import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetFilterChildrenValuesForCollection extends
    IRuntimeInteractor<IGetFilterChildrenForCollectionRequestModel, IGetFilterChildrenResponseModel> {
  
}