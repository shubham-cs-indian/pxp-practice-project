package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplierStrategy extends IConfigStrategy<IIdParameterModel, ISupplierModel> {
  
}
