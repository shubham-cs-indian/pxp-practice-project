package com.cs.core.config.strategy.usecase.user;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.user.IGetFunctionPermissionByUserIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetFunctionPermissionByUserIdStrategy extends OrientDBBaseStrategy implements IGetFunctionPermissionByUserIdStrategy{

  @Override
  public IFunctionPermissionModel execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_FUNCTION_PERMISSION_BY_USER_ID, model,
        FunctionPermissionModel.class);
  }
  
}
