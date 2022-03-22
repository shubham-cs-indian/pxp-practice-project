package com.cs.core.config.strategy.usecase.permission;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetFunctionPermissionForRoleStrategy extends OrientDBBaseStrategy implements IGetFunctionPermissionForRoleStrategy{

  @Override
  public IFunctionPermissionModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_FUNCTION_PERMISSION_FOR_ROLE, model, FunctionPermissionModel.class);
  }
  
}
