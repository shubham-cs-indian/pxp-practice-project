package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.template.ICreateCustomTemplateService;

@Service
public class CreateCustomTemplate extends AbstractCreateConfigInteractor<ICustomTemplateModel, IGetCustomTemplateModel>
    implements ICreateCustomTemplate {
  
  @Autowired
  protected ICreateCustomTemplateService createCustomTemplateService;
  
  @Override
  public IGetCustomTemplateModel executeInternal(ICustomTemplateModel model) throws Exception
  {
    return createCustomTemplateService.execute(model);
  }
}
