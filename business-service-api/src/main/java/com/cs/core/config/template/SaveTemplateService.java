package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.strategy.usecase.template.ISaveTemplateStrategy;
import com.cs.core.config.template.ISaveTemplateService;

@Service
public class SaveTemplateService extends AbstractSaveConfigService<ISaveCustomTemplateModel, IGetCustomTemplateModel>
    implements ISaveTemplateService {
  
  @Autowired
  protected ISaveTemplateStrategy saveTemplateStrategy;
  
  @Override
  public IGetCustomTemplateModel executeInternal(ISaveCustomTemplateModel idModel) throws Exception
  {
    return saveTemplateStrategy.execute(idModel);
  }
}
