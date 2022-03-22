package com.cs.core.config.target;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetTargetsByIdsService
    extends IGetConfigService<IIdsListParameterModel, IListModel<IKlass>> {
  
}
