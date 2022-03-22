package com.cs.core.config.strategy.usecase.entityCount;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.objectCount.GetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class OrientDBGetEntityCountStrategy extends OrientDBBaseStrategy implements IGetEntityCountStrategy{


  @Override
  public IGetConfigEntityResponseModel execute(IGetEntityTypeRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_ENTITY_COUNT, model, GetConfigEntityResponseModel.class);
  }
  
}

