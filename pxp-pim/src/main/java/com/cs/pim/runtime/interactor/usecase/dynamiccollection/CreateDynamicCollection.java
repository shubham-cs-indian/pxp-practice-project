package com.cs.pim.runtime.interactor.usecase.dynamiccollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.dynamiccollection.ICreateDynamicCollectionService;

@Service
public class CreateDynamicCollection extends AbstractRuntimeInteractor<IDynamicCollectionModel, IDynamicCollectionModel>
    implements ICreateDynamicCollection {
  
  @Autowired
  protected ICreateDynamicCollectionService createDynamicCollectionService;
  
  @Override
  public IDynamicCollectionModel executeInternal(IDynamicCollectionModel model) throws Exception
  {
    
    return createDynamicCollectionService.execute(model);
  }
  
}
