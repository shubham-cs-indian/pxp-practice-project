package com.cs.core.config.interactor.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.template.ICreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.smartdocument.template.ICreateSmartDocumentTemplateService;

@Service
public class CreateSmartDocumentTemplate
    extends AbstractCreateConfigInteractor<ISmartDocumentTemplateModel, ICreateSmartDocumentTemplateResponseModel>
    implements ICreateSmartDocumentTemplate {
  
  @Autowired
  protected ICreateSmartDocumentTemplateService createSmartDocumentTemplateService;
  
  @Override
  public ICreateSmartDocumentTemplateResponseModel executeInternal(ISmartDocumentTemplateModel dataModel) throws Exception
  {
    return createSmartDocumentTemplateService.execute(dataModel);
  }
}
