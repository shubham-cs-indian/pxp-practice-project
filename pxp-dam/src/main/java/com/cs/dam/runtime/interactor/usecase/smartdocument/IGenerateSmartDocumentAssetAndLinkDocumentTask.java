package com.cs.dam.runtime.interactor.usecase.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentAndLinkDocumentRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGenerateSmartDocumentAssetAndLinkDocumentTask extends
    IRuntimeInteractor<IGenerateSmartDocumentAndLinkDocumentRequestModel, IIdParameterModel> {
  
}
