package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.template.ISaveTemplatePermissionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class SaveTemplatePermissionStrategy extends OrientDBBaseStrategy
    implements ISaveTemplatePermissionStrategy {
  
  public static final String useCase = "SaveTemplatePermission";
  
  @Override
  public IIdParameterModel execute(ISaveTemplatePermissionModel model) throws Exception
  {
    return execute(useCase, model, IdParameterModel.class);
  }
}
