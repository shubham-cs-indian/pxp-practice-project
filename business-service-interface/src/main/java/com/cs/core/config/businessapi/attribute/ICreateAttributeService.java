package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;

public interface ICreateAttributeService extends ICreateConfigService<IAttributeModel, ICreateAttributeResponseModel> {
  
}
