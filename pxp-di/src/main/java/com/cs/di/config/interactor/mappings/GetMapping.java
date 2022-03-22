package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.core.config.interactor.usecase.mapping.IGetMapping;
import com.cs.di.config.mappings.IGetMappingService;

@Service
public class GetMapping extends AbstractGetConfigInteractor<IGetOutAndInboundMappingModel, IMappingModel>
    implements IGetMapping {

  @Autowired protected IGetMappingService getMappingService;

  @Override
  public IMappingModel executeInternal(IGetOutAndInboundMappingModel dataModel) throws Exception
  {
    return getMappingService.execute(dataModel);
  }
  
}
