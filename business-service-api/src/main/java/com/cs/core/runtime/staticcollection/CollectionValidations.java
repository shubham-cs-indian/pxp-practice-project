package com.cs.core.runtime.staticcollection;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.runtime.interactor.model.collections.ICreateStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionModel;

@Component
public class CollectionValidations extends Validations {
  
  public void validate(ICreateStaticCollectionModel model) throws Exception
  {
    validateLabel(model.getLabel());
  }
  
  public void validate(ISaveStaticCollectionModel saveModel) throws Exception
  {
    validateLabel(saveModel.getLabel());
  }
  
  public void validate(IDynamicCollectionModel model) throws Exception
  {
    validateLabel(model.getLabel());
  }
}
