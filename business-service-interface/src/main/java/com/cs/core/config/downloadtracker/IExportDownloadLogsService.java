package com.cs.core.config.downloadtracker;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IExportDownloadLogResponseModel;

public interface IExportDownloadLogsService
    extends IGetConfigService<IGetDownloadLogsRequestModel, IExportDownloadLogResponseModel> {
  
}
