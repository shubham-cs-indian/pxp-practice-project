package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;

@Component
public class GetConfigDetailsWithoutPermissionsStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsWithoutPermissionsStrategy {
  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_WITHOUT_PERMISSIONS, model,
        GetConfigDetailsForCustomTabModel.class);
  }
}
