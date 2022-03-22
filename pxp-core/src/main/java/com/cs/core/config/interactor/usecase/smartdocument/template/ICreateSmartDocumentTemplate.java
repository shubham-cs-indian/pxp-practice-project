package com.cs.core.config.interactor.usecase.smartdocument.template;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.template.ICreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;

public interface ICreateSmartDocumentTemplate
    extends ICreateConfigInteractor<ISmartDocumentTemplateModel, ICreateSmartDocumentTemplateResponseModel> {
  
}
