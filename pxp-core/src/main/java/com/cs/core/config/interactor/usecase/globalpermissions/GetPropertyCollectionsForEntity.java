package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.core.config.globalpermissions.IGetPropertyCollectionsForEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetPropertyCollectionsForEntityStrategy;

@Service
public class GetPropertyCollectionsForEntity extends
    AbstractGetConfigInteractor<IGetPropertyCollectionsForEntityModel, IGetGlobalPermissionsForRoleModel>
    implements IGetPropertyCollectionsForEntity {
  
  @Autowired
  protected IGetPropertyCollectionsForEntityService getPropertyCollectionsForEntityService;
  
  @Override
  public IGetGlobalPermissionsForRoleModel executeInternal(
      IGetPropertyCollectionsForEntityModel propertyCollectionModel) throws Exception
  {
    return getPropertyCollectionsForEntityService.execute(propertyCollectionModel);
  }
}
