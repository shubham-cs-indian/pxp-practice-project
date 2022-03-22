package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.strategy.usecase.mapping.IGetOutBoundMappingStrategy;

@Service
public class GetOutBoundMappingService
    extends AbstractGetConfigService<IGetOutAndInboundMappingModel, IOutBoundMappingModel>
    implements IGetOutBoundMappingService {
  
  @Autowired
  protected IGetOutBoundMappingStrategy getOutBoundMappingStrategy;
  
  @Override
  public IOutBoundMappingModel executeInternal(IGetOutAndInboundMappingModel dataModel) throws Exception
  {
    return getOutBoundMappingStrategy.execute(dataModel);
  }
}
