package com.cs.core.config.interactor.usecase.downloadtracker;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogListResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetDownloadLogs<P extends IGetDownloadLogsRequestModel, R extends IGetDownloadLogListResponseModel>
    extends AbstractGetConfigInteractor<P, R> {
  
}