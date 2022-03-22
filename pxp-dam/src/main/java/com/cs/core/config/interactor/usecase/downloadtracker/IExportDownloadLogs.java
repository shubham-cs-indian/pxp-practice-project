package com.cs.core.config.interactor.usecase.downloadtracker;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IExportDownloadLogResponseModel;

public interface IExportDownloadLogs
    extends IGetConfigInteractor<IGetDownloadLogsRequestModel, IExportDownloadLogResponseModel> {
  
}
