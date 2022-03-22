package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.core.config.globalpermissions.ICreateOrSaveGlobalPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;
import com.cs.core.config.strategy.usecase.globalpermissions.ICreateOrSaveGlobalPermissionStrategy;

@Service
public class CreateOrSaveGlobalPermission extends
    AbstractCreateConfigInteractor<ISaveGlobalPermissionModel, ICreateOrSaveGlobalPermissionResponseModel>
    implements ICreateOrSaveGlobalPermission {
  
  @Autowired
  protected ICreateOrSaveGlobalPermissionService createOrSaveGlobalPermissionService;
  
  @Override
  public ICreateOrSaveGlobalPermissionResponseModel executeInternal(ISaveGlobalPermissionModel model) throws Exception
  {
    return createOrSaveGlobalPermissionService.execute(model);
  }
}
