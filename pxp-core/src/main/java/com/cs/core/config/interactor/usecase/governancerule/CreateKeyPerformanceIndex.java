package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.governancerule.ICreateKeyPerformanceIndexService;
import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateKeyPerformanceIndex extends AbstractCreateConfigInteractor<ICreateKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel>
    implements ICreateKeyPerformanceIndex {
  
  @Autowired
  protected ICreateKeyPerformanceIndexService createKeyPerformanceIndexService;
  
  public IGetKeyPerformanceIndexModel executeInternal(ICreateKeyPerformanceIndexModel model)
      throws Exception
  {
    return createKeyPerformanceIndexService.execute(model);
  }
}
