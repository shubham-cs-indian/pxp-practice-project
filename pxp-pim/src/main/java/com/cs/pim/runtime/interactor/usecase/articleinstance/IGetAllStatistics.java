package com.cs.pim.runtime.interactor.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllStatistics extends
IRuntimeInteractor<IGetAllStatisticsRequestModel, IGetAllKPIStatisticsModel> {
  
}
