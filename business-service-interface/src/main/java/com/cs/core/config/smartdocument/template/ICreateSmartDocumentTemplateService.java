package com.cs.core.config.smartdocument.template;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.smartdocument.template.ICreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;

public interface ICreateSmartDocumentTemplateService
    extends ICreateConfigService<ISmartDocumentTemplateModel, ICreateSmartDocumentTemplateResponseModel> {
  
}
