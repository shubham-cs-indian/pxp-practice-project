package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDataStrategy extends OrientDBBaseStrategy implements IGetConfigDataStrategy {
  
  private static final String useCase = "GetConfigData";
  
  @Override
  public IGetConfigDataResponseModel execute(IGetConfigDataRequestModel model) throws Exception
  {
    return super.execute(useCase, model, GetConfigDataResponseModel.class);
  }
}
