package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getGlobalPermissionsForRoleStrategy")
public class GetGlobalPermissionsForRoleStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionsForRoleStrategy {
  
  @Override
  public IGetGlobalPermissionsForRoleModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSIONS_FOR_ROLE, requestMap,
        GetGlobalPermissionsForRoleModel.class);
  }
}
