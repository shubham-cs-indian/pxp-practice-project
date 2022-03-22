package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetKlassesForMappingModel;
import com.cs.core.config.interactor.model.endpoint.IGetKlassesForMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetKlassesForMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component public class OrientDBGetKlassesForMappingStrategy extends OrientDBBaseStrategy
    implements IGetKlassesForMappingStrategy {

  @Override public IGetKlassesForMappingModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_KLASSES_FOR_MAPPING, new HashMap<>(), GetKlassesForMappingModel.class);
  }
}
