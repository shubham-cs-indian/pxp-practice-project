package com.cs.core.config.strategy.usecase.variantconfiguration;

import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetVariantConfigurationStrategy
    extends IConfigStrategy<IModel, IVariantConfigurationModel> {
  
}
