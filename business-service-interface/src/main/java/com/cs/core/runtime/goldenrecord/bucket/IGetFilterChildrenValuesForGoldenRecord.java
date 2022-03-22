package com.cs.core.runtime.goldenrecord.bucket;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;

public interface IGetFilterChildrenValuesForGoldenRecord  extends
    IRuntimeService<IGetFilterChildrenRequestModel, /*IListModel<*/IGetFilterChildrenResponseModel/*>*/> {
  
}