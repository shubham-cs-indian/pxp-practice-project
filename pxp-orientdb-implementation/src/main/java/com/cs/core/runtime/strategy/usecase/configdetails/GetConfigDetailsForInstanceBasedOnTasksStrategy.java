package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceBasedOnTaskGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceBasedOnTaskGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component("getConfigDetailsForInstanceBasedOnTasksStrategy")
public class GetConfigDetailsForInstanceBasedOnTasksStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForInstancesBasedOnTasksStrategy {
  
  public static final String useCase = "GetConfigDetailsForInstanceBasedOnTask";
  
  @Override
  public IConfigDetailsForInstanceBasedOnTaskGetModel execute(IIdParameterModel model)
      throws Exception
  {
    return execute(useCase, model, ConfigDetailsForInstanceBasedOnTaskGetModel.class);
  }
}
