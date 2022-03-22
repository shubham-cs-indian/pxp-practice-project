package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;

public interface ISaveSmartDocument
    extends ISaveConfigInteractor<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel> {
  
}
