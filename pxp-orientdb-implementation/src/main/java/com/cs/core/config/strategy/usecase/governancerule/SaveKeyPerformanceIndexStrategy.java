package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.ISaveKPIResponseModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.SaveKPIResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveKeyPerformanceIndexStrategy extends OrientDBBaseStrategy
    implements ISaveKeyPerformanceIndexStrategy {
  
  @Override
  public ISaveKPIResponseModel execute(ISaveKeyPerformanceIndexModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("kpi", model);
    return execute(OrientDBBaseStrategy.SAVE_KEY_PERFORMANCE_INDEX, requestMap,
        SaveKPIResponseModel.class);
  }
}
