package com.cs.core.config.strategy.usecase.smartdocument.template;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.smartdocument.template.CreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ICreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class CreateSmartDocumentTemplateStrategy extends OrientDBBaseStrategy
    implements ICreateSmartDocumentTemplateStrategy {
  
  @Override
  public ICreateSmartDocumentTemplateResponseModel execute(ISmartDocumentTemplateModel model) throws Exception
  {
    return execute(CREATE_SMART_DOCUMENT_TEMPLATE, model, CreateSmartDocumentTemplateResponseModel.class);
  }
}
