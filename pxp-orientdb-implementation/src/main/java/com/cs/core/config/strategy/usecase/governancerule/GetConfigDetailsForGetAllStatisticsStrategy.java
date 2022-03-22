package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.statistics.GetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForGetAllStatisticsStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetAllStatisticsStrategy {
  
  @Override
  public IGetAllKPIStatisticsModel execute(IGetAllStatisticsRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_FOR_GET_ALL_STATISTICS, model,
        GetAllKPIStatisticsModel.class);
  }
}
