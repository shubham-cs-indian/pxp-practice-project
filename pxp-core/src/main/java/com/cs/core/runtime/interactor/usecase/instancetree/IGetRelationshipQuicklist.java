package com.cs.core.runtime.interactor.usecase.instancetree;

import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetRelationshipQuicklist extends 
  IRuntimeInteractor<IGetRelationshipQuicklistRequestModel, IGetNewInstanceTreeResponseModel> {
  
}
