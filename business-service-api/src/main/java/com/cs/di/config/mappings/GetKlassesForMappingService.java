package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetKlassesForMappingModel;
import com.cs.core.config.strategy.usecase.mapping.IGetKlassesForMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassesForMappingService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassesForMappingModel>
    implements IGetKlassesForMappingService {

  @Autowired protected IGetKlassesForMappingStrategy getKlassesForMappingStrategy;

  @Override public IGetKlassesForMappingModel executeInternal(IIdParameterModel dataModel) throws Exception
  {

    return getKlassesForMappingStrategy.execute(dataModel);
  }
}
