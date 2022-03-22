package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.di.config.mappings.IGetOutBoundMappingService;

@Service("getOutBoundMapping")
public class GetOutBoundMapping
    extends AbstractGetConfigInteractor<IGetOutAndInboundMappingModel, IOutBoundMappingModel>
    implements IGetOutBoundMapping {
  
  @Autowired
  protected IGetOutBoundMappingService getOutBoundMappingService;
  
  @Override
  public IOutBoundMappingModel executeInternal(IGetOutAndInboundMappingModel dataModel) throws Exception
  {
    return getOutBoundMappingService.execute(dataModel);
  }
}
