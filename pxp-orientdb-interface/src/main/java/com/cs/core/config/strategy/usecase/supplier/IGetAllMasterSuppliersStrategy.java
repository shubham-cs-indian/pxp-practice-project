package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllMasterSuppliersStrategy
    extends IConfigStrategy<ISupplierModel, IListModel<ISupplierModel>> {
  
}
