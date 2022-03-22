package com.cs.core.runtime.staticcollection;

import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

@Component
@SuppressWarnings({ "unchecked" })
public abstract class AbstractGetStaticCollection<P extends IGetKlassInstanceTreeStrategyModel, R extends IGetStaticCollectionModel>
    extends AbstractRuntimeService<P, R> {
  

  public IGetStaticCollectionModel executeInternal(IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return null;
    
  }
  
}