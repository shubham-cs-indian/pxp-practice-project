package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.usecase.mapping.ICreateMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;

@Service
public class CreateMappingService
    extends AbstractCreateConfigService<IMappingModel, ICreateOrSaveMappingModel>
    implements ICreateMappingService {
  
  @Autowired
  protected ICreateMappingStrategy            createMappingStrategy;
  
  @Autowired
  protected ISessionContext                   context;
  
  @Override
  public ICreateOrSaveMappingModel executeInternal(IMappingModel mappingModel) throws Exception
  {
    Validations.validateLabel(mappingModel.getLabel());
    return createMappingStrategy.execute(mappingModel);
  }

}
