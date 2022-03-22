package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.configdetails.GetConfigEntityIdsCodeNamesResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigEntityIdsCodeNamesStrategy extends OrientDBBaseStrategy
    implements IGetConfigEntityIdsCodeNamesStrategy {
  
  private static final String useCase = "GetConfigEntityIdsCodeNames";
  
  @Override
  public IGetConfigEntityIdsCodeNamesResponseModel execute(
      IGetConfigEntityIdsCodeNamesRequestModel model) throws Exception
  {
    return super.execute(useCase, model, GetConfigEntityIdsCodeNamesResponseModel.class);
  }
}
