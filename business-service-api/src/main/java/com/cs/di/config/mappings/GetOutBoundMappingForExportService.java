package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.strategy.usecase.mapping.IGetOutBoundMappingForExportStrategy;

@Service
public class GetOutBoundMappingForExportService
    extends AbstractGetConfigService<IOutBoundMappingModel, IOutBoundMappingModel>
    implements IGetOutBoundMappingForExportService {
  
  @Autowired
  protected IGetOutBoundMappingForExportStrategy getOutBoundMappingStrategy;
  
  @Override
  public IOutBoundMappingModel executeInternal(IOutBoundMappingModel dataModel) throws Exception
  {
      return getOutBoundMappingStrategy.execute(dataModel);
  }

}
