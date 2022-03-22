package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForInstanceTreeGetStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForInstanceTreeGetStrategy {
  
  public static final String useCase = "GetConfigDetailsForInstanceTreeGet";
  
  @Override
  public IConfigDetailsForInstanceTreeGetModel execute(
      IConfigDetailsForInstanceTreeGetRequestModel model) throws Exception
  {
    return execute(useCase, model, ConfigDetailsForInstanceTreeGetModel.class);
  }
}
