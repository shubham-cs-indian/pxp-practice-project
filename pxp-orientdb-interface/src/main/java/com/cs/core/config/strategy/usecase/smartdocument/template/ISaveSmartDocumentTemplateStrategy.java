package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveSmartDocumentTemplateStrategy
    extends IConfigStrategy<ISmartDocumentTemplateModel, IGetSmartDocumentTemplateWithPresetModel> {
  
}
