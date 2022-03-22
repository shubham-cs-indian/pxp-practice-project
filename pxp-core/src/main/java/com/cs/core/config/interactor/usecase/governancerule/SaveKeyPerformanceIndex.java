package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.governancerule.ISaveKeyPerformanceIndexService;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveKeyPerformanceIndex extends AbstractSaveConfigInteractor<ISaveKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel>
    implements ISaveKeyPerformanceIndex {
  
  @Autowired
  protected ISaveKeyPerformanceIndexService saveKeyPerformanceIndexService;
  
  @Override
  public IGetKeyPerformanceIndexModel executeInternal(ISaveKeyPerformanceIndexModel dataModel)
      throws Exception
  {
    return saveKeyPerformanceIndexService.execute(dataModel);
  }
}
