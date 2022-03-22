package com.cs.core.config.smartdocument.preset;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IDeleteSmartDocumentPresetService extends IDeleteConfigService<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
