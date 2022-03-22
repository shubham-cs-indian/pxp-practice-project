package com.cs.core.runtime.usecase.task;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IAssetInstanceDownloadLogsRequestModel;


public interface IInsertDownloadLogsAndCountTask extends IRuntimeService<IAssetInstanceDownloadLogsRequestModel, IIdParameterModel> {
  
}
