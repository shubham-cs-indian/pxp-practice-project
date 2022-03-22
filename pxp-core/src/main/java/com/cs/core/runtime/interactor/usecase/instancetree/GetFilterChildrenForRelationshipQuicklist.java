package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetFilterChildrenForRelationshipQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetFilterChildrenForRelationshipQuicklist extends AbstractRuntimeInteractor<IGetFilterChildrenForRelationshipQuicklistRequestModel, IGetFilterChildrenResponseModel> 
  implements IGetFilterChildrenForRelationshipQuicklist{
  
  @Autowired
  private IGetFilterChildrenForRelationshipQuicklistService getFilterChildrenForRelationshipQuicklistService;
  
  @Override
  protected IGetFilterChildrenResponseModel executeInternal(IGetFilterChildrenForRelationshipQuicklistRequestModel model) throws Exception
  {
    return getFilterChildrenForRelationshipQuicklistService.execute(model);
  }
  
 
  
}
