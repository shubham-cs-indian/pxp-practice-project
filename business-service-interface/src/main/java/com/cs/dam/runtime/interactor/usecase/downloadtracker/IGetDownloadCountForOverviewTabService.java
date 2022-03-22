package com.cs.dam.runtime.interactor.usecase.downloadtracker;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountResponseModel;

public interface IGetDownloadCountForOverviewTabService extends IRuntimeService<IGetDownloadCountRequestModel, IGetDownloadCountResponseModel>{
  
}
