package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.klassinstance.IGetStatisticsService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetStatistics
    extends AbstractRuntimeInteractor<IGetStatisticsRequestModel, IGetStatisticsResponseModel>
    implements IGetStatistics {
  
  @Autowired
  protected IGetStatisticsService getStatisticsService;

  @Override
  public IGetStatisticsResponseModel executeInternal(IGetStatisticsRequestModel dataModel) throws Exception
  {
	  return getStatisticsService.execute(dataModel);
  }


}
