package com.cs.core.runtime.interactor.usecase.instancetree;

import com.cs.core.runtime.interactor.model.instancetree.IGetLinkedInstanceQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetLinkedInstanceQuicklist extends 
  IRuntimeInteractor<IGetLinkedInstanceQuicklistRequestModel, IGetNewInstanceTreeResponseModel> {
}
