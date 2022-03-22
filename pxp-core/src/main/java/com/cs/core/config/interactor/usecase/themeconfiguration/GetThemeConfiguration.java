package com.cs.core.config.interactor.usecase.themeconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.themeconfiguration.IGetThemeConfigurationService;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetThemeConfiguration extends AbstractGetConfigInteractor<IModel, IThemeConfigurationModel> implements IGetThemeConfiguration {
  
  @Autowired
  protected IGetThemeConfigurationService getThemeConfigurationService;
  
  @Override
  public IThemeConfigurationModel executeInternal(IModel dataModel) throws Exception
  {
    return getThemeConfigurationService.execute(dataModel);
  }
}
