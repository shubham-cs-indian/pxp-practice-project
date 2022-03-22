package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.strategy.usecase.task.IGetGridTasksStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetGridTasksService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridTasksResponseModel>implements IGetGridTasksService {
  
  @Autowired
  protected IGetGridTasksStrategy getGridTasksStrategy;
  
  @Override
  public IGetGridTasksResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridTasksStrategy.execute(model);
  }
}
