package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.GetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetAllKeyPerformanceIndexStrategy extends OrientDBBaseStrategy
    implements IGetAllKeyPerformanceIndexStrategy {
  
  @Override
  public IGetAllKeyPerformanceIndexModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GETAll_KEY_PERFORMANCE_INDEX, model,
        GetAllKeyPerformanceIndexModel.class);
  }
}
