package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;

public interface IGetAllSuppliersService
    extends IGetConfigService<ISupplierModel, IListModel<IKlassInformationModel>> {
  
}
