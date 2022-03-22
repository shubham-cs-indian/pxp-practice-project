package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetNewFilterAndSortDataForLinkedInstanceQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterAndSortDataForLIQRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetNewFilterAndSortDataForLinkedInstanceQuicklist
    extends AbstractRuntimeInteractor<IGetFilterAndSortDataForLIQRequestModel, IGetNewFilterAndSortDataResponseModel>
    implements IGetNewFilterAndSortDataForLinkedInstanceQuicklist {
  
  @Autowired
  protected IGetNewFilterAndSortDataForLinkedInstanceQuicklistService getNewFilterAndSortDataForLinkedInstanceQuicklistService;
  
  @Override
  protected IGetNewFilterAndSortDataResponseModel executeInternal(IGetFilterAndSortDataForLIQRequestModel model) throws Exception
  {
    return getNewFilterAndSortDataForLinkedInstanceQuicklistService.execute(model);
  }
  
}
