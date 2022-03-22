package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;

public interface IGetAllSuppliers
    extends IGetConfigInteractor<ISupplierModel, IListModel<IKlassInformationModel>> {
  
}
