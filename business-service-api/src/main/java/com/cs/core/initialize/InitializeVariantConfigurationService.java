package com.cs.core.initialize;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.config.interactor.model.variantconfiguration.VariantConfigurationModel;
import com.cs.core.config.strategy.usecase.variantconfiguration.IGetOrCreateVariantConfigurationStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class InitializeVariantConfigurationService implements IInitializeVariantConfigurationService {
  
  @Autowired
  protected IGetOrCreateVariantConfigurationStrategy getOrCreateVariantConfigurationStrategy;
  
  @Override
  public void execute() throws Exception
  {
    InputStream stream = this.getClass().getClassLoader()
        .getResourceAsStream(InitializeDataConstants.VARIANT_CONFIGURATION);
    IVariantConfigurationModel dataModel = ObjectMapperUtil.readValue(stream,
        VariantConfigurationModel.class);
    stream.close();
    getOrCreateVariantConfigurationStrategy.execute(dataModel);
  }
}
