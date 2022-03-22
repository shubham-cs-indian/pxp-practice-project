package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.GetAllMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetAllMappingStrategy;
import org.springframework.stereotype.Component;

@Component("getAllMappingStrategy") public class OrientDBGetAllMappingStrategy
    extends OrientDBBaseStrategy implements IGetAllMappingStrategy {

  @Override public IGetAllMappingsResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(GET_ALL_MAPPINGS, model, GetAllMappingsResponseModel.class);
  }
}
