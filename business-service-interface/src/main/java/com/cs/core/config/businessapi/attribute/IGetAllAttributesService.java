package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllAttributesService extends IGetConfigService<IIdParameterModel, IListModel<IAttributeModel>> {
  
}
