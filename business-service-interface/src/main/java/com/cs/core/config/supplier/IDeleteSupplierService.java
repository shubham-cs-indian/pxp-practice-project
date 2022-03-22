package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteSupplierService
    extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
