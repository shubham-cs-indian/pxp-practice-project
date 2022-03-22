package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetNewFilterAndSortDataService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetNewFilterAndSortData
    extends AbstractRuntimeInteractor<IGetNewFilterAndSortDataRequestModel, IGetNewFilterAndSortDataResponseModel>
    implements IGetNewFilterAndSortData {
  
  @Autowired
  protected IGetNewFilterAndSortDataService getNewFilterAndSortDataService;

  
  @Override
  protected IGetNewFilterAndSortDataResponseModel executeInternal(IGetNewFilterAndSortDataRequestModel model) throws Exception
  {
    return getNewFilterAndSortDataService.execute(model);
  }

}
