package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.permission.GetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetPermissionStrategy extends OrientDBBaseStrategy implements IGetPermissionStrategy {
  
  @Override
  public IGetPermissionModel execute(IGetPermissionRequestModel model) throws Exception
  {
    return execute(GET_PERMISSION, model, GetPermissionModel.class);
  }
}
