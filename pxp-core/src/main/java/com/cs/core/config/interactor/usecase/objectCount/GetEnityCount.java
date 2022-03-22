package com.cs.core.config.interactor.usecase.objectCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.entityCount.IGetEntityCountService;
import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;

@Component
public class GetEnityCount extends AbstractGetConfigInteractor<IGetEntityTypeRequestModel, IGetConfigEntityResponseModel>
implements IGetEnityCount{
  
  @Autowired
  IGetEntityCountService getEntityCountService;
  
  @Override
  protected IGetConfigEntityResponseModel executeInternal(IGetEntityTypeRequestModel getEntityTypeRequestModel) throws Exception
  {
    return getEntityCountService.execute(getEntityTypeRequestModel);
  }
  
}
