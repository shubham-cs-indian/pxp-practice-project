package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.strategy.usecase.template.IGetTemplateStrategy;
import com.cs.core.config.template.IGetTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTemplateService
    extends AbstractGetConfigService<IIdParameterModel, IGetCustomTemplateModel>
    implements IGetTemplateService {
  
  @Autowired
  protected IGetTemplateStrategy getTemplateStrategy;
  
  @Override
  public IGetCustomTemplateModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTemplateStrategy.execute(idModel);
  }
}
