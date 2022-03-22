package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGlobalPermissionWithAllowedTemplatesStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionWithAllowedTemplatesStrategy {
  
  @Override
  public IGetGlobalPermissionWithAllowedTemplatesModel execute(
      IGetGlobalPermissionWithAllowedTemplatesRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSION_WITH_ALLOWED_TEMPLATES, model,
        GetGlobalPermissionWithAllowedTemplatesModel.class);
  }
}
