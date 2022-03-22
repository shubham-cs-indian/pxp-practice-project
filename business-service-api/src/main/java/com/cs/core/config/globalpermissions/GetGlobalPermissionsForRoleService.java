package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionsForRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetGlobalPermissionsForRoleService
    extends AbstractGetConfigService<IIdParameterModel, IGetGlobalPermissionsForRoleModel>
    implements IGetGlobalPermissionsForRoleService {
  
  @Autowired
  IGetGlobalPermissionsForRoleStrategy getGlobalPermissionsForRoleStrategy;
  
  @Override
  public IGetGlobalPermissionsForRoleModel executeInternal(IIdParameterModel propertyCollectionModel)
      throws Exception
  {
    
    return getGlobalPermissionsForRoleStrategy.execute(propertyCollectionModel);
  }
}
