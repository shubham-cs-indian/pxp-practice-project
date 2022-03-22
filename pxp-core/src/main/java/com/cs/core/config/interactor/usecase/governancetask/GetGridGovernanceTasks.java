package com.cs.core.config.interactor.usecase.governancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.strategy.usecase.governancetask.IGetGridGovernanceTasksStrategy;

@Service
public class GetGridGovernanceTasks
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTasksResponseModel>
    implements IGetGridGovernanceTasks {
  
  @Autowired
  protected IGetGridGovernanceTasksStrategy getGridGovernanceTasksStrategy;
  
  @Override
  public IGetGridTasksResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridGovernanceTasksStrategy.execute(model);
  }
}
