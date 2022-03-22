package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetRelationshipQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetRelationshipQuicklist extends
    AbstractRuntimeInteractor<IGetRelationshipQuicklistRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetRelationshipQuicklist {
  
  @Autowired
  protected IGetRelationshipQuicklistService getRelationshipQuicklistService;
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeInternal(IGetRelationshipQuicklistRequestModel model) throws Exception
  {
    return getRelationshipQuicklistService.execute(model);
  }
  
 
}
