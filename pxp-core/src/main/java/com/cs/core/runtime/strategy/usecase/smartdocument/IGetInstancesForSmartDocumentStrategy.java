package com.cs.core.runtime.strategy.usecase.smartdocument;

import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetInstancesForSmartDocumentStrategy extends
    IRuntimeStrategy<IGetInstancesForSmartDocumentRequestModel, IGetInstancesForSmartDocumentResponseModel> {
  
}
