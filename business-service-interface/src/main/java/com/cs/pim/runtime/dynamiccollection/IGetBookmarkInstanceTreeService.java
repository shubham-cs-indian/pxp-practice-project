package com.cs.pim.runtime.dynamiccollection;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeForBookmarkResponseModel;

public interface IGetBookmarkInstanceTreeService
    extends IRuntimeService<IGetInstanceTreeRequestModel, IGetNewInstanceTreeForBookmarkResponseModel> {
  
}
