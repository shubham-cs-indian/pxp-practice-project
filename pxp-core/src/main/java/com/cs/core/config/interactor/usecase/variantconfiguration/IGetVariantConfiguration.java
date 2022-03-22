package com.cs.core.config.interactor.usecase.variantconfiguration;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetVariantConfiguration
    extends IGetConfigInteractor<IModel, IVariantConfigurationModel> {
  
}
