package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetSuppliersByIdsService
    extends IGetConfigService<IIdsListParameterModel, IListModel<IKlass>> {
  
}
