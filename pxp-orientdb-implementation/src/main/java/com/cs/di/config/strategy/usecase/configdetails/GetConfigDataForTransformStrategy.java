package com.cs.di.config.strategy.usecase.configdetails;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.configdetails.GetConfigDataForTransformResponseModel;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformRequestModel;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformResponseModel;

@Component
public class GetConfigDataForTransformStrategy extends OrientDBBaseStrategy implements IGetConfigDataForTransformStrategy {
  
  private static final String useCase = "GetConfigDetailsForTransformation";

  @Override
  public IGetConfigDataForTransformResponseModel execute(IGetConfigDataForTransformRequestModel model) throws Exception
  {
    return super.execute(useCase, model, GetConfigDataForTransformResponseModel.class);
  }
}
