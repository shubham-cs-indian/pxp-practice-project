package com.cs.core.config.template;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTemplateService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
