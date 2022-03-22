package com.cs.core.config.themeconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.strategy.usecase.themeconfiguration.ISaveThemeConfigurationStrategy;

@Service
public class SaveThemeConfigurationService
    extends AbstractSaveConfigService<IThemeConfigurationModel, IThemeConfigurationModel>
    implements ISaveThemeConfigurationService {
  
  @Autowired
  protected ISaveThemeConfigurationStrategy saveThemeConfigurationStrategy;
  
  @Override
  public IThemeConfigurationModel executeInternal(IThemeConfigurationModel dataModel) throws Exception
  {
    return saveThemeConfigurationStrategy.execute(dataModel);
  }
}
