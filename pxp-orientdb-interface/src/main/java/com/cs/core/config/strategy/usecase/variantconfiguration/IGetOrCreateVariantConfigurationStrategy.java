package com.cs.core.config.strategy.usecase.variantconfiguration;

import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateVariantConfigurationStrategy
    extends IConfigStrategy<IVariantConfigurationModel, IVariantConfigurationModel> {
  
}
