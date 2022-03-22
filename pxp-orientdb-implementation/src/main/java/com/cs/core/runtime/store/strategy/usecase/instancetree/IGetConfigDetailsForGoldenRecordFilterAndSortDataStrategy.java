package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;

public interface IGetConfigDetailsForGoldenRecordFilterAndSortDataStrategy  extends     
    IConfigStrategy<IConfigDetailsForFilterAndSortInfoRequestModel, IGetNewFilterAndSortDataResponseModel> {
}
