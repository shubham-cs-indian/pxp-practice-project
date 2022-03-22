package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteKeyPerformanceIndexService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
