package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.core.config.strategy.usecase.mapping.IGetMappingStrategy;

@Service
public class GetMappingService extends AbstractGetConfigService<IGetOutAndInboundMappingModel, IMappingModel>
    implements IGetMappingService {

  @Autowired protected IGetMappingStrategy getMappingStrategy;

  @Override public IMappingModel executeInternal(IGetOutAndInboundMappingModel dataModel) throws Exception
  {
    if (dataModel.getId() != null) {
      return getMappingStrategy.execute(dataModel);
    }
    else {
      return new MappingModel();
    }
  }

}
