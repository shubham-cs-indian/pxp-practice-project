package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetFilterChildrenValuesService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetFilterChildrenValues extends
    AbstractRuntimeInteractor<IGetFilterChildrenRequestModel,IGetFilterChildrenResponseModel>
    implements IGetFilterChildrenValues {

  @Autowired
  protected IGetFilterChildrenValuesService getFilterChildrenValuesService;


  @Override
  protected IGetFilterChildrenResponseModel executeInternal(IGetFilterChildrenRequestModel model) throws Exception
  {
    return getFilterChildrenValuesService.execute(model);
  }
  
 
  
}
