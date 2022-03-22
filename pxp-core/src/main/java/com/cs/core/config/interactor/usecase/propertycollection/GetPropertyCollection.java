package com.cs.core.config.interactor.usecase.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.propertycollection.IGetPropertyCollectionService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetPropertyCollection extends AbstractGetConfigInteractor<IIdParameterModel, IGetPropertyCollectionModel>
    implements IGetPropertyCollection {
  
  @Autowired
  protected IGetPropertyCollectionService getPropertyCollectionService;
  
  @Override
  public IGetPropertyCollectionModel executeInternal(IIdParameterModel propertyCollectionModel) throws Exception
  {
    return getPropertyCollectionService.execute(propertyCollectionModel);
  }
}
