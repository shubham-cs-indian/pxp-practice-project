package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.template.ISaveTemplateService;

@Service
public class SaveTemplate extends AbstractSaveConfigInteractor<ISaveCustomTemplateModel, IGetCustomTemplateModel> implements ISaveTemplate {
  
  @Autowired
  protected ISaveTemplateService saveTemplateService;
  
  @Override
  public IGetCustomTemplateModel executeInternal(ISaveCustomTemplateModel idModel) throws Exception
  {
    return saveTemplateService.execute(idModel);
  }
}
