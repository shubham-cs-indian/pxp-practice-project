package com.cs.di.config.businessapi.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.strategy.usecase.processevent.IGetProcessEventForDashboardStrategy;

@Service
public class GetProcessEventForDashboardService extends
    AbstractGetConfigService<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel> implements IGetProcessEventForDashboardService {
  
  @Autowired
  protected IGetProcessEventForDashboardStrategy getProcessEventForDashboardStrategy;
  
  @Override
  protected IGetConfigDataEntityResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getProcessEventForDashboardStrategy.execute(dataModel);
  }
}
