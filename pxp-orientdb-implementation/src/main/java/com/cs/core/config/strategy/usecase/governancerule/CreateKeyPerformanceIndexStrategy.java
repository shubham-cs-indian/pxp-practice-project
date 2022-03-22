package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.GetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CreateKeyPerformanceIndexStrategy extends OrientDBBaseStrategy
    implements ICreateKeyPerformanceIndexStrategy {
  
  @Override
  public IGetKeyPerformanceIndexModel execute(ICreateKeyPerformanceIndexModel model)
      throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("kpi", model);
    return execute(OrientDBBaseStrategy.CREATE_KEY_PERFORMANCE_INDEX, requestMap,
        GetKeyPerformanceIndexModel.class);
  }
}
