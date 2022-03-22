package com.cs.core.config.textasset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetTextAssetsByIdsService
    extends IGetConfigService<IIdsListParameterModel, IListModel<IKlass>> {
  
}
