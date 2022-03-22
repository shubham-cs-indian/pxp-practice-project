package com.cs.core.config.strategy.usecase.variant;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.variantcontext.GetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForAutoCreateTIVStrategy;

@Component("getConfigDetailsForAutoCreateTIVStrategy")
public class GetConfigDetailsForAutoCreateTIVStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForAutoCreateTIVStrategy {
  
  @Override
  public IGetConfigDetailsForAutoCreateTIVResponseModel execute(IGetConfigDetailsForAutoCreateTIVRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_AUTO_CREATE_TIV, model,
        GetConfigDetailsForAutoCreateTIVResponseModel.class);
  }
  
}
