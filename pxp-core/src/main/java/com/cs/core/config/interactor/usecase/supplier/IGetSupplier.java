package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplier extends IGetConfigInteractor<IIdParameterModel, ISupplierModel> {
  
}
