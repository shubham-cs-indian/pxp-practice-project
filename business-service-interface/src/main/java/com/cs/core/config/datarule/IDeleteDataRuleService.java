package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteDataRuleService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
