package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveSmartDocumentStrategy
    extends IConfigStrategy<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel> {
  
}
