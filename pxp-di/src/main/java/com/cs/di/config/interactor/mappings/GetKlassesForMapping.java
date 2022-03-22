package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetKlassesForMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.mappings.IGetKlassesForMappingService;

@Service
public class GetKlassesForMapping
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassesForMappingModel>
    implements IGetKlassesForMapping {

  @Autowired protected IGetKlassesForMappingService getKlassesForMappingService;

  @Override public IGetKlassesForMappingModel executeInternal(IIdParameterModel dataModel) throws Exception
  {

    return getKlassesForMappingService.execute(dataModel);
  }
}
