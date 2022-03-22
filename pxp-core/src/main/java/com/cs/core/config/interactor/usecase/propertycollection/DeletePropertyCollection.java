package com.cs.core.config.interactor.usecase.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.propertycollection.IDeletePropertyCollectionService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeletePropertyCollection extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeletePropertyCollection {
  
  @Autowired
  protected IDeletePropertyCollectionService deletePropertyCollectionService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel propertyCollectionModel) throws Exception
  {
    return deletePropertyCollectionService.execute(propertyCollectionModel);
  }
}
