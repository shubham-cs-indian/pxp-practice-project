package com.cs.core.config.strategy.usecase.entityCount;

import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetEntityCountStrategy extends IConfigStrategy<IGetEntityTypeRequestModel, IGetConfigEntityResponseModel>{
  
}
