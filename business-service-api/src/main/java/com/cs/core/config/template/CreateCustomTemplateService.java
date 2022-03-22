package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.strategy.usecase.template.ICreateCustomTemplateStrategy;
import com.cs.core.config.template.ICreateCustomTemplateService;

@Service
public class CreateCustomTemplateService extends AbstractCreateConfigService<ICustomTemplateModel, IGetCustomTemplateModel>
    implements ICreateCustomTemplateService {
  
  @Autowired
  protected ICreateCustomTemplateStrategy createCustomTemplateStrategy;
  
  @Override
  public IGetCustomTemplateModel executeInternal(ICustomTemplateModel model) throws Exception
  {
    return createCustomTemplateStrategy.execute(model);
  }
}
