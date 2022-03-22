package com.cs.core.config.interactor.usecase.downloadtracker;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogListResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;

public interface IGetDownloadLogs
    extends IGetConfigInteractor<IGetDownloadLogsRequestModel, IGetDownloadLogListResponseModel> {
  
}
