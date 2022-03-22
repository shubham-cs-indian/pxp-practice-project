package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.di.config.mappings.ICreateOutBoundMappingService;

@Service("createOutBoundMapping")
public class CreateOutBoundMapping extends
    AbstractCreateConfigInteractor<IOutBoundMappingModel, ICreateOutBoundMappingResponseModel>
    implements ICreateOutBoundMapping {
  
  @Autowired
  protected ICreateOutBoundMappingService createOutBoundMappingService;
  
  @Override
  public ICreateOutBoundMappingResponseModel executeInternal(IOutBoundMappingModel mappingModel)
      throws Exception
  {
    return createOutBoundMappingService.execute(mappingModel);
  }
}
