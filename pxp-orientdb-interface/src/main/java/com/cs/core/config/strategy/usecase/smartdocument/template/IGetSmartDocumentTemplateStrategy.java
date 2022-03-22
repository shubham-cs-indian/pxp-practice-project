package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocumentTemplateStrategy
    extends IConfigStrategy<IIdParameterModel, IGetSmartDocumentTemplateWithPresetModel> {
  
}
