package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateSupplierStrategy
    extends IConfigStrategy<ISupplierModel, IGetKlassEntityWithoutKPModel> {
  
}
