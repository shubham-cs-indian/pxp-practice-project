package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteGoldenRecordRulesService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
