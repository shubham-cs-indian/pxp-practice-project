package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetLinkedInstanceQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetLinkedInstanceQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetLinkedInstanceQuicklist
    extends AbstractRuntimeInteractor<IGetLinkedInstanceQuicklistRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetLinkedInstanceQuicklist {
  
  @Autowired
  protected IGetLinkedInstanceQuicklistService getLinkedInstanceQuicklistService;
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeInternal(IGetLinkedInstanceQuicklistRequestModel model) throws Exception
  {
    return getLinkedInstanceQuicklistService.execute(model);

  }
  
}
