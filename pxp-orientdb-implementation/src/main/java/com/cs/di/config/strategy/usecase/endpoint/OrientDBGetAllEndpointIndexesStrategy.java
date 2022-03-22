package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllEndpointIndexesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("getAllEndpointIndexesStrategy") public class OrientDBGetAllEndpointIndexesStrategy
    extends OrientDBBaseStrategy implements IGetAllEndpointIndexesStrategy {

  public static final String useCase = "GetAllEndpointIndexes";

  @Override public IIdsListParameterModel execute(IEndpointModel model) throws Exception
  {
    return execute(useCase, new HashMap<>(), IdsListParameterModel.class);
  }

}
