package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForSingleEntityModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.user.GlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGlobalPermissionForSingleEntityStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionForSingleEntityStrategy {
  
  @Override
  public IGlobalPermissionWithAllowedModuleEntitiesModel execute(
      IGetGlobalPermissionForSingleEntityModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSION_FOR_SINGLE_ENTITY, model,
        GlobalPermissionWithAllowedModuleEntitiesModel.class);
  }
}
