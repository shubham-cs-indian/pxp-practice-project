package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IDeleteSmartDocumentPresetStrategy
    extends IConfigStrategy<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
