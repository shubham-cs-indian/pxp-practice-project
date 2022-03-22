package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteAttributeService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
