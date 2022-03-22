package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;

public interface IGetConfigDetailsForGetAllStatisticsStrategy
    extends IConfigStrategy<IGetAllStatisticsRequestModel, IGetAllKPIStatisticsModel> {
  
}
