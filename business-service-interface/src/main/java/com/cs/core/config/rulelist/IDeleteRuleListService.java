package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteRuleListService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
