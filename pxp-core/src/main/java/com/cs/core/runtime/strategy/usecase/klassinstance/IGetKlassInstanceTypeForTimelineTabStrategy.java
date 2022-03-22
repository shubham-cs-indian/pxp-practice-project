package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IInstanceTypeGetRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstanceTypeForTimelineTabStrategy
    extends IRuntimeStrategy<IInstanceTypeGetRequestModel, IKlassInstanceTypeModel> {
  
}
