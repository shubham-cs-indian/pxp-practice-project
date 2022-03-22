package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocument
    extends IGetConfigInteractor<IIdParameterModel, IGetSmartDocumentWithTemplateModel> {
  
}
