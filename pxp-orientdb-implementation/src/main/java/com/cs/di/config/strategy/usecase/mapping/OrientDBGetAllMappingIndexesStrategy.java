package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetAllMappingIndexesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("getAllMappingIndexesStrategy") public class OrientDBGetAllMappingIndexesStrategy
    extends OrientDBBaseStrategy implements IGetAllMappingIndexesStrategy {

  public static final String useCase = "GetAllEndpointIndexes";

  @Override public IIdsListParameterModel execute(IMappingModel model) throws Exception
  {
    return execute(useCase, new HashMap<>(), IdsListParameterModel.class);
  }

}
