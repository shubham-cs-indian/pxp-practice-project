package com.cs.di.config.strategy.usecase.mapping;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.OutBoundMappingModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetOutBoundMappingForExportStrategy;

@Component("getOutBoundMappingForExportStrategy")
public class GetOutBoundMappingForExportStrategy extends OrientDBBaseStrategy
    implements IGetOutBoundMappingForExportStrategy {
  
  @Override
  public IOutBoundMappingModel execute(IOutBoundMappingModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_OUTBOUND_MAPPING_FOR_EXPORT, model, OutBoundMappingModel.class);
  }
}