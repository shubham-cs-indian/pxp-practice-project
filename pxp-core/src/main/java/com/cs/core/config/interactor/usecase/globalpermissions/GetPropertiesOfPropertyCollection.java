package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.core.config.globalpermissions.IGetPropertiesOfPropertyCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetPropertiesOfPropertyCollectionStrategy;

@Service
public class GetPropertiesOfPropertyCollection extends
    AbstractGetConfigInteractor<IGetPropertiesOfPropertyCollectionModel, IGetGlobalPermissionsForRoleModel>
    implements IGetPropertiesOfPropertyCollection {
  
  @Autowired
  protected IGetPropertiesOfPropertyCollectionService getPropertiesOfPropertyCollectionService;
  
  @Override
  public IGetGlobalPermissionsForRoleModel executeInternal(
      IGetPropertiesOfPropertyCollectionModel propertyCollectionModel) throws Exception
  {
    
    return getPropertiesOfPropertyCollectionService.execute(propertyCollectionModel);
  }
}
