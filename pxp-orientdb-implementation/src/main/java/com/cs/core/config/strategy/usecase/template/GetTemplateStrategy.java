package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.customtemplate.GetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetTemplateStrategy extends OrientDBBaseStrategy implements IGetTemplateStrategy {
  
  public static final String useCase = "GetTemplate";
  
  @Override
  public IGetCustomTemplateModel execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, model, GetCustomTemplateModel.class);
  }
}
