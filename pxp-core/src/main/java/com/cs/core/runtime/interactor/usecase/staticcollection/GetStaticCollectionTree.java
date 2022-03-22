package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetStaticCollectionTreeService;

@Service
public class GetStaticCollectionTree extends AbstractRuntimeInteractor<IIdParameterModel, IListModel<ICollectionModel>>
    implements IGetStaticCollectionTree {
  
  @Autowired
  protected IGetStaticCollectionTreeService getStaticCollectionTreeService;
  
  public IListModel<ICollectionModel> executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getStaticCollectionTreeService.execute(idModel);
    
  }
  
}
