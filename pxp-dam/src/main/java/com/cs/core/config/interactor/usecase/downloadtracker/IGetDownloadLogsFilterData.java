package com.cs.core.config.interactor.usecase.downloadtracker;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataRequestModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataResponseModel;


public interface IGetDownloadLogsFilterData extends IGetConfigInteractor<IGetDownloadLogsFilterDataRequestModel, IGetDownloadLogsFilterDataResponseModel> {

}
