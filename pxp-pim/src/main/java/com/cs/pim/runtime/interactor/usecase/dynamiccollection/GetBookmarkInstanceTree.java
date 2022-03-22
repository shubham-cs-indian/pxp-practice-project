package com.cs.pim.runtime.interactor.usecase.dynamiccollection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeForBookmarkResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.dynamiccollection.IGetBookmarkInstanceTreeService;

@Service
public class GetBookmarkInstanceTree
    extends AbstractRuntimeInteractor<IGetInstanceTreeRequestModel, IGetNewInstanceTreeForBookmarkResponseModel>
    implements IGetBookmarkInstanceTree {
  
  @Autowired
  protected IGetBookmarkInstanceTreeService getBookmarkInstanceTreeService;

  
  @Override
  public IGetNewInstanceTreeForBookmarkResponseModel executeInternal(IGetInstanceTreeRequestModel model) throws Exception
  {
    return getBookmarkInstanceTreeService.execute(model);
  }
  
}
