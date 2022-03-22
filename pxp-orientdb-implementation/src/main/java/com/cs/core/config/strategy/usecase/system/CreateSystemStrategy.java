package com.cs.core.config.strategy.usecase.system;

import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.interactor.model.system.SystemModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateSystemStrategy extends OrientDBBaseStrategy implements ICreateSystemStrategy {
  
  @Override
  public ISystemModel execute(ICreateSystemModel model) throws Exception
  {
    return execute(CREATE_SYSTEM, model, SystemModel.class);
  }
}
