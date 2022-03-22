package com.cs.core.runtime.instancetree;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetRelationshipQuicklistRequestModel;

public interface IGetRelationshipQuicklistService extends
    IRuntimeService<IGetRelationshipQuicklistRequestModel, IGetNewInstanceTreeResponseModel> {
  
}
