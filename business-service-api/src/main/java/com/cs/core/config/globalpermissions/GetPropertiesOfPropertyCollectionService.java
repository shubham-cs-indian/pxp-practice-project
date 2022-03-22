package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetPropertiesOfPropertyCollectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPropertiesOfPropertyCollectionService extends AbstractGetConfigService<IGetPropertiesOfPropertyCollectionModel, IGetGlobalPermissionsForRoleModel>
    implements IGetPropertiesOfPropertyCollectionService {
  
  @Autowired
  IGetPropertiesOfPropertyCollectionStrategy getPropertiesOfPropertyCollectionStrategy;
  
  @Override
  public IGetGlobalPermissionsForRoleModel executeInternal(
      IGetPropertiesOfPropertyCollectionModel propertyCollectionModel) throws Exception
  {
    
    return getPropertiesOfPropertyCollectionStrategy.execute(propertyCollectionModel);
  }
}
