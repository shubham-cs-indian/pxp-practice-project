package com.cs.core.config.strategy.usecase.variantconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.interactor.model.variantconfiguration.VariantConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;


@Component
public class GetVariantConfigurationStrategy extends OrientDBBaseStrategy
    implements IGetVariantConfigurationStrategy {
  
  @Override
  public IVariantConfigurationModel execute(IModel model) throws Exception
  {
    return execute(GET_VARIANT_CONFIGURATION, model, VariantConfigurationModel.class);
  }
  
  @Override
  public String getUsecase()
  {
    return "Get Variant Configuration Strategy";
  }
}
