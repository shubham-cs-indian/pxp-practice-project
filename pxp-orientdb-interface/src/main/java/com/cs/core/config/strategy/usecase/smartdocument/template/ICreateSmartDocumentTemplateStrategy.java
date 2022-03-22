package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.smartdocument.template.ICreateSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateSmartDocumentTemplateStrategy
    extends IConfigStrategy<ISmartDocumentTemplateModel, ICreateSmartDocumentTemplateResponseModel> {
  
}
