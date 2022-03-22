package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateAttributeStrategy
    extends IConfigStrategy<IAttributeModel, ICreateAttributeResponseModel> {
  
}
