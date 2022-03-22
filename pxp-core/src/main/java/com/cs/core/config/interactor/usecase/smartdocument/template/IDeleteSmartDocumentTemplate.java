package com.cs.core.config.interactor.usecase.smartdocument.template;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IDeleteSmartDocumentTemplate
    extends IDeleteConfigInteractor<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
