package com.cs.pim.runtime.interactor.usecase.dynamiccollection;


import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeForBookmarkResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetBookmarkInstanceTree extends
    IRuntimeInteractor<IGetInstanceTreeRequestModel, IGetNewInstanceTreeForBookmarkResponseModel> {
  
}
