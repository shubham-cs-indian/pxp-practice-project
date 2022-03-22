package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetSuppliersByIds
    extends IGetConfigInteractor<IIdsListParameterModel, IListModel<IKlass>> {
  
}
