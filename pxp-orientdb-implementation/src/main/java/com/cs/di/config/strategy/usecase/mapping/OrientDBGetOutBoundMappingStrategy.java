package com.cs.di.config.strategy.usecase.mapping;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.OutBoundMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetOutBoundMappingStrategy;

@Component("getOutBoundMappingStrategy")
public class OrientDBGetOutBoundMappingStrategy extends OrientDBBaseStrategy
    implements IGetOutBoundMappingStrategy {
  
  @Override
  public IOutBoundMappingModel execute(IGetOutAndInboundMappingModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_OUTBOUND_MAPPING, model, OutBoundMappingModel.class);
  }
}
