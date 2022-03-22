package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRQFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForRQResponseModel;

public interface IGetConfigDetailsForGetRQFilterAndSortDataStrategy 
  extends IConfigStrategy<IConfigDetailsForRQFilterAndSortDataRequestModel, IGetNewFilterAndSortDataForRQResponseModel> {
  
}
