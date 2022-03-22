package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGlobalPermissionForRuntimeEntitiesStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionForRuntimeEntitiesStrategy {
  
  @Override
  public IGetGlobalPermissionForRuntimeEntitiesResponseModel execute(
      IGetGlobalPermissionModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSION_FOR_RUNTIME_ENTITIES, model,
        GetGlobalPermissionForRuntimeEntitiesResponseModel.class);
  }
}
