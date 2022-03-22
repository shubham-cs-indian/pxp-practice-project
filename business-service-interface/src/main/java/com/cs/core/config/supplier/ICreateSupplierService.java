package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;

public interface ICreateSupplierService
    extends ICreateConfigService<ISupplierModel, IGetKlassEntityWithoutKPModel> {
  
}
