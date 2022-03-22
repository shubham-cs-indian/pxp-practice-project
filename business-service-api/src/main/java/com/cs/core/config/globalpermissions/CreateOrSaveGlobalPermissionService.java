package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.globalpermissions.ICreateOrSaveGlobalPermissionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateOrSaveGlobalPermissionService extends AbstractCreateConfigService<ISaveGlobalPermissionModel, ICreateOrSaveGlobalPermissionResponseModel>
    implements ICreateOrSaveGlobalPermissionService {
  
  @Autowired
  protected ICreateOrSaveGlobalPermissionStrategy createOrSaveGlobalPermissionStrategy;
  
  @Override
  public ICreateOrSaveGlobalPermissionResponseModel executeInternal(ISaveGlobalPermissionModel model) throws Exception
  {
    return createOrSaveGlobalPermissionStrategy.execute(model);
  }
}
