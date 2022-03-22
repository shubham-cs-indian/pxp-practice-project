package com.cs.core.runtime.klassinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;

public interface IGetAllStatisticsService extends IRuntimeService<IGetAllStatisticsRequestModel, IGetAllKPIStatisticsModel> {
  
}
