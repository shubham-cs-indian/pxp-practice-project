package com.cs.pim.runtime.dynamiccollection;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetBaseEntityIidsByBookmarkIdService extends
    IRuntimeService<IGetInstanceTreeRequestModel, IIdsListParameterModel> {
  
}
