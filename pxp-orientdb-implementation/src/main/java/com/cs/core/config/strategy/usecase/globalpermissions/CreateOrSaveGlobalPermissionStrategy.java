package com.cs.core.config.strategy.usecase.globalpermissions;


import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.globalpermissions.CreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class CreateOrSaveGlobalPermissionStrategy extends OrientDBBaseStrategy
    implements ICreateOrSaveGlobalPermissionStrategy {
  
  @Override
  public ICreateOrSaveGlobalPermissionResponseModel execute(ISaveGlobalPermissionModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.CREATE_OR_SAVE_GLOBAL_PERMISSION, model, CreateOrSaveGlobalPermissionResponseModel.class);
  }
}
