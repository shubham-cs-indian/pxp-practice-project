package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.ISaveStaticCollectionService;

@Service
public class SaveStaticCollection extends AbstractRuntimeInteractor<ISaveStaticCollectionModel, IGetStaticCollectionModel>
    implements ISaveStaticCollection {
  
  @Autowired
  protected ISaveStaticCollectionService saveStaticCollectionService;
  
  @Override
  protected IGetStaticCollectionModel executeInternal(ISaveStaticCollectionModel model) throws Exception
  {
    return saveStaticCollectionService.execute(model);
  }
  
}
