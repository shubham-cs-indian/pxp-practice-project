package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.statistics.GetStatisticsResponseModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsResponseModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForGetStatisticsStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetStatisticsStrategy {
  
  @Override
  public IGetStatisticsResponseModel execute(IGetStatisticsRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_FOR_GET_STATISTICS, model,
        GetStatisticsResponseModel.class);
  }
}
