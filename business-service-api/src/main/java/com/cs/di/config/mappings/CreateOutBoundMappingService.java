package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.strategy.usecase.mapping.ICreateOutBoundMappingStrategy;

@Service
public class CreateOutBoundMappingService extends
    AbstractCreateConfigService<IOutBoundMappingModel, ICreateOutBoundMappingResponseModel>
    implements ICreateOutBoundMappingService {
  
  @Autowired
  protected ICreateOutBoundMappingStrategy createOutBoundMappingStrategy;
  
  @Override
  public ICreateOutBoundMappingResponseModel executeInternal(IOutBoundMappingModel mappingModel)
      throws Exception
  {
    return createOutBoundMappingStrategy.execute(mappingModel);
  }
}
