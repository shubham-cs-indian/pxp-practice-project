package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.di.config.mappings.IGetOutBoundMappingForExportService;

@Service("getOutBoundMappingForExport")
public class GetOutBoundMappingForExport
    extends AbstractGetConfigInteractor<IOutBoundMappingModel, IOutBoundMappingModel>
    implements IGetOutBoundMappingForExport {
  
  @Autowired
  protected IGetOutBoundMappingForExportService getOutBoundMappingService;
  
  @Override
  public IOutBoundMappingModel executeInternal(IOutBoundMappingModel dataModel) throws Exception
  {
      return getOutBoundMappingService.execute(dataModel);
  }

}
