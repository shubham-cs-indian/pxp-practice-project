package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAttributeStrategy extends IConfigStrategy<IIdParameterModel, IAttributeModel> {
  
}
