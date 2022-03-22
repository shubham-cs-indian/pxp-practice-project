package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.core.config.governancerule.IGetKeyPerformanceIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.usecase.governancerule.IGetKeyPerformanceIndexStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKeyPerformanceIndex extends AbstractGetConfigInteractor<IIdParameterModel, IGetKeyPerformanceIndexModel>
    implements IGetKeyPerformanceIndex {
  
  @Autowired
  protected IGetKeyPerformanceIndexService getKeyPerformanceIndexService;
  
  @Override
  public IGetKeyPerformanceIndexModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getKeyPerformanceIndexService.execute(dataModel);
  }
}
