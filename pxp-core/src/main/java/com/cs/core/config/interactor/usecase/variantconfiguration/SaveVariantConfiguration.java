package com.cs.core.config.interactor.usecase.variantconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.strategy.usecase.variantconfiguration.ISaveVariantConfigurationStrategy;

@Service
public class SaveVariantConfiguration
    extends AbstractSaveConfigInteractor<IVariantConfigurationModel, IVariantConfigurationModel>
    implements ISaveVariantConfiguration {
  
  @Autowired
  protected ISaveVariantConfigurationStrategy saveVariantConfigurationStrategy;
  
  @Override
  public IVariantConfigurationModel executeInternal(IVariantConfigurationModel dataModel) throws Exception
  {
    return saveVariantConfigurationStrategy.execute(dataModel);
    
  }

}
