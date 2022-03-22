package com.cs.di.config.strategy.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetMappingStrategy;

@Component("getMappingStrategy")
public class OrientDBGetMappingStrategy extends OrientDBBaseStrategy
    implements IGetMappingStrategy {
  
  public static final String useCase = "GetMapping";

  @Override public IMappingModel execute(IGetOutAndInboundMappingModel model) throws Exception
  {
   
    return execute(useCase, model, MappingModel.class);
  }

}
