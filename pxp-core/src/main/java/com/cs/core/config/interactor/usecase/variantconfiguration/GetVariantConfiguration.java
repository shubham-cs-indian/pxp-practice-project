package com.cs.core.config.interactor.usecase.variantconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.strategy.usecase.variantconfiguration.IGetVariantConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetVariantConfiguration
    extends AbstractGetConfigInteractor<IModel, IVariantConfigurationModel>
    implements IGetVariantConfiguration {
  
  @Autowired
  protected IGetVariantConfigurationStrategy getVariantConfigurationStrategy;
  
  @Override
  public IVariantConfigurationModel executeInternal(IModel dataModel) throws Exception
  {
    return getVariantConfigurationStrategy.execute(dataModel);
  }
  
}
