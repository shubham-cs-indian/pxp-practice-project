package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.core.config.globalpermissions.IGetGlobalPermissionsForRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionsForRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetGlobalPermissionsForRole
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetGlobalPermissionsForRoleModel>
    implements IGetGlobalPermissionsForRole {
  
  @Autowired
  protected IGetGlobalPermissionsForRoleService getGlobalPermissionsForRoleStrategy;
  
  @Override
  public IGetGlobalPermissionsForRoleModel executeInternal(IIdParameterModel propertyCollectionModel)
      throws Exception
  {
    
    return getGlobalPermissionsForRoleStrategy.execute(propertyCollectionModel);
  }
}
