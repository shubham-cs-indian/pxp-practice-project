package com.cs.core.config.strategy.usecase.smartdocument.fetchdata;

import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetInstanceForSmartDocument extends
    IRuntimeInteractor<IGetEntityForSmartDocumentRequestModel, ISmartDocumentKlassInstanceDataModel> {
  
}
