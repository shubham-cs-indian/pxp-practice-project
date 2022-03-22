package com.cs.dam.runtime.interactor.usecase.downloadtracker;

import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountResponseModel;

public interface IGetDownloadCountForOverviewTab
    extends IRuntimeInteractor<IGetDownloadCountRequestModel, IGetDownloadCountResponseModel> {
  
}
