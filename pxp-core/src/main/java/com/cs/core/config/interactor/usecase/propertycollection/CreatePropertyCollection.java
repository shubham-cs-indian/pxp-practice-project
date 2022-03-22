package com.cs.core.config.interactor.usecase.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.propertycollection.ICreatePropertyCollectionService;

@Service
public class CreatePropertyCollection extends AbstractCreateConfigInteractor<IPropertyCollectionModel, IGetPropertyCollectionModel>
    implements ICreatePropertyCollection {
  
  @Autowired
  protected ICreatePropertyCollectionService createPropertyCollectionService;
  
  @Override
  public IGetPropertyCollectionModel executeInternal(IPropertyCollectionModel propertyCollectionModel) throws Exception
  {
    return createPropertyCollectionService.execute(propertyCollectionModel);
  }
}
