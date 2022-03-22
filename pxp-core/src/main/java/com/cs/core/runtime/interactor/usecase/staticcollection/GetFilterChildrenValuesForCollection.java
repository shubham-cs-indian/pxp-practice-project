package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetFilterChildrenValuesForCollectionService;

@Service
public class GetFilterChildrenValuesForCollection
    extends AbstractRuntimeInteractor<IGetFilterChildrenForCollectionRequestModel, IGetFilterChildrenResponseModel>
    implements IGetFilterChildrenValuesForCollection {
  
  @Autowired
  protected IGetFilterChildrenValuesForCollectionService getFilterChildrenValuesForCollectionService;
  
  @Override
  protected IGetFilterChildrenResponseModel executeInternal(IGetFilterChildrenForCollectionRequestModel model) throws Exception
  {
    return getFilterChildrenValuesForCollectionService.execute(model);
  }
  
}
