package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.governancerule.IGetAllKeyPerformanceIndexService;
import com.cs.core.config.interactor.model.governancerule.IGetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllKeyPerformanceIndex extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllKeyPerformanceIndexModel>
    implements IGetAllKeyPerformanceIndex {
  
  @Autowired
  protected IGetAllKeyPerformanceIndexService getAllKeyPerformanceIndexService;
  
  @Override
  public IGetAllKeyPerformanceIndexModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return getAllKeyPerformanceIndexService.execute(dataModel);
  }
}
