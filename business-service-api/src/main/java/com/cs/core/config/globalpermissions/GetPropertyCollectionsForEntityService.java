package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetPropertyCollectionsForEntityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPropertyCollectionsForEntityService extends AbstractGetConfigService<IGetPropertyCollectionsForEntityModel, IGetGlobalPermissionsForRoleModel>
    implements IGetPropertyCollectionsForEntityService {
  
  @Autowired
  IGetPropertyCollectionsForEntityStrategy getPropertyCollectionsForEntityStrategy;
  
  @Override
  public IGetGlobalPermissionsForRoleModel executeInternal(
      IGetPropertyCollectionsForEntityModel propertyCollectionModel) throws Exception
  {
    
    return getPropertyCollectionsForEntityStrategy.execute(propertyCollectionModel);
  }
}
