package com.cs.core.config.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.strategy.usecase.smartdocument.template.IGetSmartDocumentTemplateStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSmartDocumentTemplateService extends AbstractGetConfigService<IIdParameterModel, IGetSmartDocumentTemplateWithPresetModel>
    implements IGetSmartDocumentTemplateService {
  
  @Autowired
  protected IGetSmartDocumentTemplateStrategy getSmartDocumentTemplateStrategy;
  
  @Override
  public IGetSmartDocumentTemplateWithPresetModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentTemplateStrategy.execute(dataModel);
  }
}
