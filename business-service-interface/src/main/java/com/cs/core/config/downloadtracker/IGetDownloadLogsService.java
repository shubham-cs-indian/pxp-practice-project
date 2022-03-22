package com.cs.core.config.downloadtracker;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogListResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;

public interface IGetDownloadLogsService
    extends IGetConfigService<IGetDownloadLogsRequestModel, IGetDownloadLogListResponseModel> {
  
}
