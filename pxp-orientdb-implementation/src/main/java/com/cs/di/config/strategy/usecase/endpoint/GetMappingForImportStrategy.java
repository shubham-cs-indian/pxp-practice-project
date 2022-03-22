package com.cs.di.config.strategy.usecase.endpoint;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.endpoint.GetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetMappingForImportStrategy;

@Component("GetMappingForImportStrategy")
public class GetMappingForImportStrategy extends OrientDBBaseStrategy
    implements IGetMappingForImportStrategy {

  public static final String useCase = "GetMappingForImport";

  @Override public IGetMappingForImportResponseModel execute(IGetMappingForImportRequestModel model)
      throws Exception
  {
    return execute(useCase, model, GetMappingForImportResponseModel.class);
  }

}
