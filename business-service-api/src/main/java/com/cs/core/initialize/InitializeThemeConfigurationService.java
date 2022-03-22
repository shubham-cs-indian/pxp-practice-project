package com.cs.core.initialize;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.usecase.themeconfiguration.IGetOrCreateThemeConfigurationStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class InitializeThemeConfigurationService implements IInitializeThemeConfigurationService {
  
  @Autowired
  protected IGetOrCreateThemeConfigurationStrategy getOrCreateThemeConfigurationStrategy;
  
  @Override
  public void execute() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.THEME_CONFIGURATION);
    IThemeConfigurationModel dataModel = ObjectMapperUtil.readValue(stream,
        ThemeConfigurationModel.class);
    stream.close();
    getOrCreateThemeConfigurationStrategy.execute(dataModel);
  }
}
