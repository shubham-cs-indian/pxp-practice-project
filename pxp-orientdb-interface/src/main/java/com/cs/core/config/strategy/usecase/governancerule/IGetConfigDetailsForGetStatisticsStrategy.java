package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsResponseModel;

public interface IGetConfigDetailsForGetStatisticsStrategy
    extends IConfigStrategy<IGetStatisticsRequestModel, IGetStatisticsResponseModel> {
  
}
