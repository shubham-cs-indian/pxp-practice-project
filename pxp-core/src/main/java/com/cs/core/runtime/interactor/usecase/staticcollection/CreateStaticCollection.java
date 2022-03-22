package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.ICreateStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IStaticCollectionModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.ICreateStaticCollectionService;

@Service
public class CreateStaticCollection extends AbstractRuntimeInteractor<ICreateStaticCollectionModel, IStaticCollectionModel> 
  implements ICreateStaticCollection {

  @Autowired
  protected ICreateStaticCollectionService createStaticCollectionService;
  
  @Override
  public IStaticCollectionModel executeInternal(ICreateStaticCollectionModel dataModel) throws Exception
  {
    return createStaticCollectionService.execute(dataModel);
  }
  
}