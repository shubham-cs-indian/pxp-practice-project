package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsWithIdsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsForKpiResponseModel;

public interface IGetConfigDetailsForGetStatisticsForContentStrategy
    extends IConfigStrategy<IGetAllStatisticsWithIdsRequestModel, IGetStatisticsForKpiResponseModel> {
  
}
