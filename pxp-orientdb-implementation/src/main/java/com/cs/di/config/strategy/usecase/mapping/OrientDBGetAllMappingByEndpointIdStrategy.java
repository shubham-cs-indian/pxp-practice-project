package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.interactor.model.mapping.IRuntimeMappingModel;
import com.cs.core.config.interactor.model.mapping.RuntimeMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetAllMappingByEndpointIdStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAllMappingByEndpointIdStrategy")
public class OrientDBGetAllMappingByEndpointIdStrategy extends OrientDBBaseStrategy
    implements IGetAllMappingByEndpointIdStrategy {

  public static final String useCase = "GetAllMappingByEndpointId";
  
  @Override
  public IRuntimeMappingModel execute(IGetMappingForImportRequestModel dataModel) throws Exception
  {
    return execute(useCase, dataModel, RuntimeMappingModel.class);
  }
}