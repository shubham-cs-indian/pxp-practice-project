package com.cs.core.config.downloadtracker;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataRequestModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataResponseModel;

public interface IGetDownloadLogsFilterDataService
extends IGetConfigService<IGetDownloadLogsFilterDataRequestModel, IGetDownloadLogsFilterDataResponseModel> {

}
