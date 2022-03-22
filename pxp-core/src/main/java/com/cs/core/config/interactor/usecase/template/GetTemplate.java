package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.template.IGetTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTemplate extends AbstractGetConfigInteractor<IIdParameterModel, IGetCustomTemplateModel> implements IGetTemplate {
  
  @Autowired
  protected IGetTemplateService getTemplateService;
  
  @Override
  public IGetCustomTemplateModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTemplateService.execute(idModel);
  }
}
