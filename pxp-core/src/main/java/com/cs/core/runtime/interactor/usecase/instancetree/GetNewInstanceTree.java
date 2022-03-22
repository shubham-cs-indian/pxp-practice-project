package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.instancetree.IGetNewInstanceTreeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetNewInstanceTree extends AbstractRuntimeInteractor<IGetInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetNewInstanceTree {
  
  @Autowired
  private IGetNewInstanceTreeService getNewInstanceTreeService;
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeInternal(IGetInstanceTreeRequestModel model) throws Exception
  {
    return getNewInstanceTreeService.execute(model);
  }
}
