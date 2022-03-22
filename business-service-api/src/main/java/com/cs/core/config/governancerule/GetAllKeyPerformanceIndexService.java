package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.governancerule.IGetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.governancerule.IGetAllKeyPerformanceIndexStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllKeyPerformanceIndexService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllKeyPerformanceIndexModel>
    implements IGetAllKeyPerformanceIndexService {
  
  @Autowired
  protected IGetAllKeyPerformanceIndexStrategy getAllKeyPerformanceIndexStrategy;
  
  @Override
  public IGetAllKeyPerformanceIndexModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return getAllKeyPerformanceIndexStrategy.execute(dataModel);
  }
}
