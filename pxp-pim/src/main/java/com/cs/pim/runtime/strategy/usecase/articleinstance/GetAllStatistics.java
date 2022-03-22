package com.cs.pim.runtime.strategy.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.klassinstance.IGetAllStatisticsService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetAllStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllStatistics
    extends AbstractRuntimeInteractor<IGetAllStatisticsRequestModel, IGetAllKPIStatisticsModel>
    implements IGetAllStatistics {
  
  @Autowired
  protected IGetAllStatisticsService getAllStatisticsService;
  
  @Override
  public IGetAllKPIStatisticsModel executeInternal(IGetAllStatisticsRequestModel dataModel) throws Exception
  {
    return getAllStatisticsService.execute(dataModel);
  }
}