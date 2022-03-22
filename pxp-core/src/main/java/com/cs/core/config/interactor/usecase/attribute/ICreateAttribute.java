package com.cs.core.config.interactor.usecase.attribute;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;

public interface ICreateAttribute
    extends ICreateConfigInteractor<IAttributeModel, ICreateAttributeResponseModel> {
  
}
