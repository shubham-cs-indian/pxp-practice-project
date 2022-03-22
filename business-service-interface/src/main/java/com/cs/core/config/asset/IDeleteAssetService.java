package com.cs.core.config.asset;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteAssetService
    extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
