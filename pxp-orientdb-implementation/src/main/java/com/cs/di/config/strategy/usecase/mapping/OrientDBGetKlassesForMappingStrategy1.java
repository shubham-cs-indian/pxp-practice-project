package com.cs.di.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.GetKlassesForMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetKlassesForMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetKlassesForMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component public class OrientDBGetKlassesForMappingStrategy1 extends OrientDBBaseStrategy
    implements IGetKlassesForMappingStrategy {

  @Override public IGetKlassesForMappingModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_KLASSES_FOR_MAPPING, new HashMap<>(), GetKlassesForMappingModel.class);
  }
}
