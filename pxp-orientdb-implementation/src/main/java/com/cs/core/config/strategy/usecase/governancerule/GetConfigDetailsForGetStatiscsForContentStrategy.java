package com.cs.core.config.strategy.usecase.governancerule;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.statistics.GetStatisticsForKpiResponseModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsWithIdsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsForKpiResponseModel;

@Component
public class GetConfigDetailsForGetStatiscsForContentStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetStatisticsForContentStrategy {
  
  @Override
  public IGetStatisticsForKpiResponseModel execute(IGetAllStatisticsWithIdsRequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_FOR_GET__STATISTICS_FOR_CONTENT, model,
        GetStatisticsForKpiResponseModel.class);
  }
}
