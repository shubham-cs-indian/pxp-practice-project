package com.cs.core.config.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.smartdocument.template.ICreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.usecase.smartdocument.template.ICreateSmartDocumentTemplateStrategy;

@Service
public class CreateSmartDocumentTemplateService
    extends AbstractCreateConfigService<ISmartDocumentTemplateModel, ICreateSmartDocumentTemplateResponseModel>
    implements ICreateSmartDocumentTemplateService {
  
  @Autowired
  protected ICreateSmartDocumentTemplateStrategy createSmartDocumentTemplateStrategy;
  
  @Override
  public ICreateSmartDocumentTemplateResponseModel executeInternal(ISmartDocumentTemplateModel dataModel) throws Exception
  {
    return createSmartDocumentTemplateStrategy.execute(dataModel);
  }
}
