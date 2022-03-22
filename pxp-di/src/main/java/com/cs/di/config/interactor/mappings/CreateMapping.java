package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.di.config.mappings.ICreateMappingService;

@Service
public class CreateMapping
    extends AbstractCreateConfigInteractor<IMappingModel, ICreateOrSaveMappingModel>
    implements ICreateMapping {
  
  @Autowired
  protected ICreateMappingService            createMappingService;
  
  @Override
  public ICreateOrSaveMappingModel executeInternal(IMappingModel mappingModel) throws Exception
  {
    return createMappingService.execute(mappingModel);
  }

}
