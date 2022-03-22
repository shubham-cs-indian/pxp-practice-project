package com.cs.core.config.entityCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;
import com.cs.core.config.strategy.usecase.entityCount.IGetEntityCountStrategy;

@Service
public class GetEntityCountService extends AbstractGetConfigService<IGetEntityTypeRequestModel, IGetConfigEntityResponseModel>
implements IGetEntityCountService{
  
  @Autowired
  IGetEntityCountStrategy getEntityCountStrategy;
  
  @Override
  protected IGetConfigEntityResponseModel executeInternal(IGetEntityTypeRequestModel model) throws Exception
  {
    return getEntityCountStrategy.execute(model);
  }
  
}
