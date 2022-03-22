package com.cs.core.config.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.strategy.usecase.propertycollection.ICreatePropertyCollectionStrategy;

@Service
public class CreatePropertyCollectionService extends AbstractCreateConfigService<IPropertyCollectionModel, IGetPropertyCollectionModel>
    implements ICreatePropertyCollectionService {
  
  @Autowired
  protected ICreatePropertyCollectionStrategy createPropertyCollectionStrategy;
  
  @Override
  public IGetPropertyCollectionModel executeInternal(IPropertyCollectionModel propertyCollectionModel) throws Exception
  {
    PropertyCollectionValidations.validateCreatePropertyCollection(propertyCollectionModel);
    return createPropertyCollectionStrategy.execute(propertyCollectionModel);
  }
}
