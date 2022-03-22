package com.cs.core.config.strategy.usecase.variantconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.interactor.model.variantconfiguration.VariantConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SaveVariantConfigurationStrategy extends OrientDBBaseStrategy
    implements ISaveVariantConfigurationStrategy {
  
  @Override
  public IVariantConfigurationModel execute(IVariantConfigurationModel model) throws Exception
  {
    return execute(SAVE_VARIANT_CONFIGURATION, model.getEntity(), VariantConfigurationModel.class);
  }
  
}
