package com.cs.core.config.interactor.usecase.permission;

import com.cs.core.config.permission.IGetFunctionPermissionForRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.strategy.usecase.permission.IGetFunctionPermissionForRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetFunctionPermissionForRole extends AbstractGetConfigInteractor<IIdParameterModel, IFunctionPermissionModel> implements IGetFunctionPermissionForRole {
  
  @Autowired
  protected IGetFunctionPermissionForRoleService getFunctionPermissionForRoleService;

  @Override
  protected IFunctionPermissionModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getFunctionPermissionForRoleService.execute(model);
  }
  
}