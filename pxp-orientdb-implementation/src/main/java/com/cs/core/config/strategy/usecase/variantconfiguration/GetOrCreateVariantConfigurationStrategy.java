package com.cs.core.config.strategy.usecase.variantconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.interactor.model.variantconfiguration.VariantConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetOrCreateVariantConfigurationStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateVariantConfigurationStrategy {
  
  @Override
  public IVariantConfigurationModel execute(IVariantConfigurationModel model) throws Exception
  {
    return execute(GET_OR_CREATE_VARIANT_CONFIGURATION, model, VariantConfigurationModel.class);
  }
}
