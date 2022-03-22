package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetFilterChildrenForLinkedInstanceQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForLIQRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetFilterChildrenForLinkedInstanceQuicklist extends AbstractRuntimeInteractor<IGetFilterChildrenForLIQRequestModel, IGetFilterChildrenResponseModel> 
  implements IGetFilterChildrenForLinkedInstanceQuicklist {

  @Autowired
  protected IGetFilterChildrenForLinkedInstanceQuicklistService getFilterChildrenForLinkedInstanceQuicklistService;
  
  @Override
  protected IGetFilterChildrenResponseModel executeInternal(IGetFilterChildrenForLIQRequestModel model) throws Exception
  {
    return getFilterChildrenForLinkedInstanceQuicklistService.execute(model);
  }
  

}