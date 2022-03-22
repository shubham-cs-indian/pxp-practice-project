package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.model.governancerule.BulkDeleteKeyPerformanceIndexStrategyModel;
import com.cs.core.config.strategy.model.governancerule.IBulkDeleteKeyPerformanceIndexStrategyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteKeyPerformanceIndexStrategy extends OrientDBBaseStrategy
    implements IDeleteKeyPerformanceIndexStrategy {
  
  @Override
  public IBulkDeleteKeyPerformanceIndexStrategyModel execute(IIdsListParameterModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.DELETE_KEY_PERFORMANCE_INDEX, model,
        BulkDeleteKeyPerformanceIndexStrategyModel.class);
  }
}
