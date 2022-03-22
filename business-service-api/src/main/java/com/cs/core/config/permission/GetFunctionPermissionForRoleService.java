package com.cs.core.config.permission;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.strategy.usecase.permission.IGetFunctionPermissionForRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetFunctionPermissionForRoleService extends AbstractGetConfigService<IIdParameterModel, IFunctionPermissionModel>
    implements IGetFunctionPermissionForRoleService {
  
  @Autowired
  IGetFunctionPermissionForRoleStrategy getFunctionPermissionForRoleStrategy;

  @Override
  protected IFunctionPermissionModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getFunctionPermissionForRoleStrategy.execute(model);
  }
  
}