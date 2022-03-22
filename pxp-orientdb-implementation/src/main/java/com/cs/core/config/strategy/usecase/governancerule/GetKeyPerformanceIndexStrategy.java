package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.GetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetKeyPerformanceIndexStrategy extends OrientDBBaseStrategy
    implements IGetKeyPerformanceIndexStrategy {
  
  @Override
  public IGetKeyPerformanceIndexModel execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_KEY_PERFORMANCE_INDEX, model,
        GetKeyPerformanceIndexModel.class);
  }
}
