package com.cs.core.config.interactor.usecase.themeconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.themeconfiguration.ISaveThemeConfigurationService;

@Service
public class SaveThemeConfiguration extends AbstractSaveConfigInteractor<IThemeConfigurationModel, IThemeConfigurationModel>
    implements ISaveThemeConfiguration {
  
  @Autowired
  protected ISaveThemeConfigurationService saveThemeConfigurationService;
  
  @Override
  public IThemeConfigurationModel executeInternal(IThemeConfigurationModel dataModel) throws Exception
  {
    return saveThemeConfigurationService.execute(dataModel);
  }
}
