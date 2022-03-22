package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.template.GetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.template.IGetTemplatePermissionStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetTemplatePermissionStrategy extends OrientDBBaseStrategy
    implements IGetTemplatePermissionStrategy {
  
  public static final String useCase = "GetTemplatePermission";
  
  @Override
  public IGetTemplatePermissionModel execute(IPermissionRequestModel model) throws Exception
  {
    return execute(useCase, model, GetTemplatePermissionModel.class);
  }
}
