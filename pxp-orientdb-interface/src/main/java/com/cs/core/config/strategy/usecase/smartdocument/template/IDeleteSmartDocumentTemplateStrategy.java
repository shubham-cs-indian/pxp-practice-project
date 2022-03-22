package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IDeleteSmartDocumentTemplateStrategy
    extends IConfigStrategy<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
