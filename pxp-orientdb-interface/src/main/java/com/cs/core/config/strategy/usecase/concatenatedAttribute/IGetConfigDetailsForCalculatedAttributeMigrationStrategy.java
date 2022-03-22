package com.cs.core.config.strategy.usecase.concatenatedAttribute;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributeRequestModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributesResponseModel;

public interface IGetConfigDetailsForCalculatedAttributeMigrationStrategy
    extends IConfigStrategy<IConfigDetailsForCalculatedAttributeRequestModel, IConfigDetailsForCalculatedAttributesResponseModel> {
  
}
