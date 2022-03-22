package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogListResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetDownloadLogsStrategy extends IConfigStrategy<IGetDownloadLogsRequestModel, IGetDownloadLogListResponseModel> {
  
}
