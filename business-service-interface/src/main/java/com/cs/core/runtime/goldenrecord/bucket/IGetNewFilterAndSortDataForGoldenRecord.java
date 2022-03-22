package com.cs.core.runtime.goldenrecord.bucket;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;

public interface IGetNewFilterAndSortDataForGoldenRecord extends
  IRuntimeService<IGetNewFilterAndSortDataRequestModel, IGetNewFilterAndSortDataResponseModel> {
  
}
