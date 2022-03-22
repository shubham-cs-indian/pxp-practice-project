package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.usecase.governancerule.IGetKeyPerformanceIndexStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetKeyPerformanceIndexService extends AbstractGetConfigService<IIdParameterModel, IGetKeyPerformanceIndexModel>
    implements IGetKeyPerformanceIndexService {
  
  @Autowired
  protected IGetKeyPerformanceIndexStrategy getKeyPerformanceIndexStrategy;
  
  @Override
  public IGetKeyPerformanceIndexModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getKeyPerformanceIndexStrategy.execute(dataModel);
  }
}
