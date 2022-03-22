package com.cs.core.config.interactor.usecase.themeconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.themeconfiguration.ISaveDefaultThemeConfigurationService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class SaveDefaultThemeConfiguration extends AbstractSaveConfigInteractor<IVoidModel, IThemeConfigurationModel>
    implements ISaveDefaultThemeConfiguration {
  
  @Autowired
  protected ISaveDefaultThemeConfigurationService saveDefaultThemeConfigurationService;
  
  @Override
  public IThemeConfigurationModel executeInternal(IVoidModel dataModel) throws Exception
  {
    return saveDefaultThemeConfigurationService.execute(dataModel);
  }
}
