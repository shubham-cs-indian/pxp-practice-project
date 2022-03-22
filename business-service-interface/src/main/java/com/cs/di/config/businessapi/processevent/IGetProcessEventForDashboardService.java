package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetProcessEventForDashboardService
    extends IGetConfigService<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel> {
  
}
