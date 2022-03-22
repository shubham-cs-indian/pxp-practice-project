package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetNewFilterAndSortDataForCollectionService;

@Service
public class GetNewFilterAndSortDataForCollection
    extends AbstractRuntimeInteractor<IGetNewFilterAndSortDataForCollectionRequestModel, IGetNewFilterAndSortDataResponseModel>
    implements IGetNewFilterAndSortDataForCollection {
  
  @Autowired
  protected IGetNewFilterAndSortDataForCollectionService getNewFilterAndSortDataForCollectionService;
  
  @Override
  protected IGetNewFilterAndSortDataResponseModel executeInternal(IGetNewFilterAndSortDataForCollectionRequestModel model) throws Exception
  {
    return getNewFilterAndSortDataForCollectionService.execute(model);
  }
  
}