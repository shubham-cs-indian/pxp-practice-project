package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.di.config.mappings.IGetAllMappingsService;

@Service
public class GetAllMappings
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllMappingsResponseModel>
    implements IGetAllMappings {

  @Autowired protected IGetAllMappingsService getAllMappingService;

  @Override public IGetAllMappingsResponseModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return getAllMappingService.execute(dataModel);
  }

}
