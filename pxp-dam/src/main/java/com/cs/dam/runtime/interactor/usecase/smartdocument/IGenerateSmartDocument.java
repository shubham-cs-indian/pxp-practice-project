package com.cs.dam.runtime.interactor.usecase.smartdocument;

import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGenerateSmartDocument extends
    IRuntimeInteractor<IGenerateSmartDocumentRequestModel, IGenerateSmartDocumentResponseModel> {
  
}
