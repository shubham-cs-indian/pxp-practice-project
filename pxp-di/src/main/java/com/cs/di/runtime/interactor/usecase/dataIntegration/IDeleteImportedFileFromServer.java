package com.cs.di.runtime.interactor.usecase.dataIntegration;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteImportedFileFromServer
    extends IRuntimeInteractor<IListModel<String>, IBulkDeleteReturnModel> {
  
}
