package com.cs.core.config.smartdocument.template;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IDeleteSmartDocumentTemplateService extends IDeleteConfigService<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
