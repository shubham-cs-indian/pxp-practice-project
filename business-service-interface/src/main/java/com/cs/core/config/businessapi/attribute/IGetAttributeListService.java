package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;

public interface IGetAttributeListService extends IGetConfigService<IIdParameterModel, IListModel<IAttributeInfoModel>> {
  
}
