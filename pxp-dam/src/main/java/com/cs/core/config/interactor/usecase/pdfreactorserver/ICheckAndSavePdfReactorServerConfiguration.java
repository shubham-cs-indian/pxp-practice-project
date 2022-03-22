package com.cs.core.config.interactor.usecase.pdfreactorserver;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;

public interface ICheckAndSavePdfReactorServerConfiguration
    extends ISaveConfigInteractor<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel> {
  
}
