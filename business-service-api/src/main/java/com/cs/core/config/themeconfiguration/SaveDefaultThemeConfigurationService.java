package com.cs.core.config.themeconfiguration;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.usecase.themeconfiguration.ISaveDefaultThemeConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Service
public class SaveDefaultThemeConfigurationService extends AbstractSaveConfigService<IVoidModel, IThemeConfigurationModel>
    implements ISaveDefaultThemeConfigurationService {
  
  @Autowired
  protected ISaveDefaultThemeConfigurationStrategy saveDefaultThemeConfigurationStrategy;
  
  @Override
  public IThemeConfigurationModel executeInternal(IVoidModel dataModel) throws Exception
  {
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(InitializeDataConstants.THEME_CONFIGURATION);
    IThemeConfigurationModel model = ObjectMapperUtil.readValue(stream, ThemeConfigurationModel.class);
    return saveDefaultThemeConfigurationStrategy.execute(model);
  }
}
