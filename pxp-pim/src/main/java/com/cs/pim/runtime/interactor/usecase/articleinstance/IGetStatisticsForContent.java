package com.cs.pim.runtime.interactor.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsForContentResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetStatisticsForContent extends IRuntimeInteractor<IIdAndTypeModel, IGetStatisticsForContentResponseModel> {
  
}