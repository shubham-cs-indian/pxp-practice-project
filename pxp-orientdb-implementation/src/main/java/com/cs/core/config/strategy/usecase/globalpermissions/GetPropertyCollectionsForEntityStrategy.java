package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getPropertyCollectionsForEntityStrategy")
public class GetPropertyCollectionsForEntityStrategy extends OrientDBBaseStrategy
    implements IGetPropertyCollectionsForEntityStrategy {
  
  @Override
  public IGetGlobalPermissionsForRoleModel execute(IGetPropertyCollectionsForEntityModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("globalPermissions", model);
    return execute(OrientDBBaseStrategy.GET_PROPERTY_COLLECTIONS_FOR_ENTITY, requestMap,
        GetGlobalPermissionsForRoleModel.class);
  }
}
