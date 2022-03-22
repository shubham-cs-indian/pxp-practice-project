package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDetailsToFetchDataForSmartDocumentStrategy extends
    IConfigStrategy<IGetConfigDetailsToFetchDataForSmartDocumentRequestModel, IGetConfigDetailsToFetchDataForSmartDocumentResponseModel> {
  
}
