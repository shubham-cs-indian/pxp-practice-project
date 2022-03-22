package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.customtemplate.GetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateCustomTemplateStrategy extends OrientDBBaseStrategy
    implements ICreateCustomTemplateStrategy {
  
  public static final String useCase = "CreateCustomTemplate";
  
  @Override
  public IGetCustomTemplateModel execute(ICustomTemplateModel model) throws Exception
  {
    return execute(useCase, model, GetCustomTemplateModel.class);
  }
}
