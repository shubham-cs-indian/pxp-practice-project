package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.usecase.smartdocument.GetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsToFetchDataForSmartDocumentStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsToFetchDataForSmartDocumentStrategy {
  
  @Override
  public IGetConfigDetailsToFetchDataForSmartDocumentResponseModel execute(
      IGetConfigDetailsToFetchDataForSmartDocumentRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_TO_FETCH_DATA_SMART_DOCUMENT, model,
        GetConfigDetailsToFetchDataForSmartDocumentResponseModel.class);
  }
}
