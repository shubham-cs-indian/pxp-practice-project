package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;

public interface ICreateSupplier
    extends ICreateConfigInteractor<ISupplierModel, IGetKlassEntityWithoutKPModel> {
  
}
