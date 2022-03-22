package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getPropertiesOfPropertyCollectionStrategy")
public class GetPropertiesOfPropertyCollectionStrategy extends OrientDBBaseStrategy
    implements IGetPropertiesOfPropertyCollectionStrategy {
  
  @Override
  public IGetGlobalPermissionsForRoleModel execute(IGetPropertiesOfPropertyCollectionModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("globalPermissions", model);
    return execute(OrientDBBaseStrategy.GET_PROPERTIES_OF_PROPERTY_COLLECTION, requestMap,
        GetGlobalPermissionsForRoleModel.class);
  }
}
