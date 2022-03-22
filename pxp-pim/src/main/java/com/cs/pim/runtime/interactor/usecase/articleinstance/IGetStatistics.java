package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetStatistics extends
IRuntimeInteractor<IGetStatisticsRequestModel, IGetStatisticsResponseModel> {
  
}
