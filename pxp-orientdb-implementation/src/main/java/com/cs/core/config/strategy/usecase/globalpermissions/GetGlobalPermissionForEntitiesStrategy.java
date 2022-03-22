package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForEntitiesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForEntitiesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetGlobalPermissionForEntitiesStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionForEntitiesStrategy {
  
  @Override
  public IGetGlobalPermissionForEntitiesModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("roleId", model.getId());
    return execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSION_FOR_ENTITIES, requestMap,
        GetGlobalPermissionForEntitiesModel.class);
  }
}
