package com.cs.core.config.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.strategy.usecase.propertycollection.IGetPropertyCollectionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetPropertyCollectionService extends AbstractGetConfigService<IIdParameterModel, IGetPropertyCollectionModel>
    implements IGetPropertyCollectionService {
  
  @Autowired
  protected IGetPropertyCollectionStrategy getPropertyCollectionStrategy;
  
  @Override
  public IGetPropertyCollectionModel executeInternal(IIdParameterModel propertyCollectionModel) throws Exception
  {
    return getPropertyCollectionStrategy.execute(propertyCollectionModel);
  }
}
