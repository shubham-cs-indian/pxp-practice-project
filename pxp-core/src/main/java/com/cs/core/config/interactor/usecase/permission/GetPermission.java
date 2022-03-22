package com.cs.core.config.interactor.usecase.permission;

import com.cs.core.config.permission.IGetPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;
import com.cs.core.config.strategy.usecase.permission.IGetPermissionStrategy;

@Service
public class GetPermission
    extends AbstractGetConfigInteractor<IGetPermissionRequestModel, IGetPermissionModel>
    implements IGetPermission {
  
  @Autowired
  protected IGetPermissionService getPermissionService;
  
  @Override
  public IGetPermissionModel executeInternal(IGetPermissionRequestModel requestModel) throws Exception
  {
    return getPermissionService.execute(requestModel);
  }
}
