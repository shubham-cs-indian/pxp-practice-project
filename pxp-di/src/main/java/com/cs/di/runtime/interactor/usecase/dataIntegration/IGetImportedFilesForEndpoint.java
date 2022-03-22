package com.cs.di.runtime.interactor.usecase.dataIntegration;

import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.runtime.interactor.model.fileinstance.IGetFileNameListResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetImportedFilesForEndpoint
    extends IRuntimeInteractor<IIdPaginationModel, IGetFileNameListResponseModel> {
  
}
