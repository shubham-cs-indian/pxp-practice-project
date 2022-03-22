package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.core.config.strategy.usecase.mapping.IGetAllMappingStrategy;

@Service
public class GetAllMappingsService
    extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllMappingsResponseModel>
    implements IGetAllMappingsService {

  @Autowired protected IGetAllMappingStrategy getAllMappingStrategy;

  @Override public IGetAllMappingsResponseModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return getAllMappingStrategy.execute(dataModel);
  }

}
