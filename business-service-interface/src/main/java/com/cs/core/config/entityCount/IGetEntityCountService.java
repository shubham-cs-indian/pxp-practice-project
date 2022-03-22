package com.cs.core.config.entityCount;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;

public interface IGetEntityCountService extends IGetConfigService<IGetEntityTypeRequestModel, IGetConfigEntityResponseModel>{
  
}
