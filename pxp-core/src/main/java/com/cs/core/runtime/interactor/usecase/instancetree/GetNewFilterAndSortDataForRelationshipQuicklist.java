package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetNewFilterAndSortDataForRelationshipQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterAndSortDataForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForRQResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetNewFilterAndSortDataForRelationshipQuicklist extends AbstractRuntimeInteractor<IGetFilterAndSortDataForRelationshipQuicklistRequestModel, IGetNewFilterAndSortDataForRQResponseModel> 
  implements IGetNewFilterAndSortDataForRelationshipQuicklist {
  
  @Autowired
  private IGetNewFilterAndSortDataForRelationshipQuicklistService       getNewFilterAndSortDataForRelationshipQuicklistService;

  @Override
  protected IGetNewFilterAndSortDataForRQResponseModel executeInternal(IGetFilterAndSortDataForRelationshipQuicklistRequestModel model)
      throws Exception
  {
    return getNewFilterAndSortDataForRelationshipQuicklistService.execute(model);
  }
  
 
  
}
