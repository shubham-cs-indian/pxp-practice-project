package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.instancetree.IGetCollectionInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetCollectionInstanceTreeService;

@Service
public class GetCollectionInstanceTree
    extends AbstractRuntimeInteractor<IGetCollectionInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetCollectionInstanceTree {
  
  @Autowired
  protected IGetCollectionInstanceTreeService getCollectionInstanceTreeService;
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeInternal(IGetCollectionInstanceTreeRequestModel model) throws Exception
  {
    return getCollectionInstanceTreeService.execute(model);
  }
  
}