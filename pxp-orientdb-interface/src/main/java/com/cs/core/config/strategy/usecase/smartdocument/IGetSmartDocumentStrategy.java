package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocumentStrategy
    extends IConfigStrategy<IIdParameterModel, IGetSmartDocumentWithTemplateModel> {
  
}
