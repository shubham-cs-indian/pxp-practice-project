package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetStaticCollectionService;

@Component
public class GetStaticCollection extends AbstractRuntimeInteractor<IGetKlassInstanceTreeStrategyModel, IGetStaticCollectionModel>
    implements IGetStaticCollection {
  
  @Autowired
  protected IGetStaticCollectionService getStaticCollectionService;
  
  @Override
  protected IGetStaticCollectionModel executeInternal(IGetKlassInstanceTreeStrategyModel model) throws Exception
  {
    return getStaticCollectionService.execute(model);
  }
  
}
