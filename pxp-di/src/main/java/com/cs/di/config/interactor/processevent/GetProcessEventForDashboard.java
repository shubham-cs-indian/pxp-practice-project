package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.businessapi.processevent.IGetProcessEventForDashboardService;

@Service("getProcessEventForDashboard")
public class GetProcessEventForDashboard extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>
    implements IGetProcessEventForDashboard {
  
  @Autowired
  protected IGetProcessEventForDashboardService getProcessEventForDashboardService;
  
  @Override
  protected IGetConfigDataEntityResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getProcessEventForDashboardService.execute(dataModel);
  }
}
