package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplierService extends IGetConfigService<IIdParameterModel, ISupplierModel> {
  
}
