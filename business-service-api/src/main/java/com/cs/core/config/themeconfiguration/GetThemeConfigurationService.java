package com.cs.core.config.themeconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.strategy.usecase.themeconfiguration.IGetThemeConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetThemeConfigurationService extends AbstractGetConfigService<IModel, IThemeConfigurationModel>
    implements IGetThemeConfigurationService {
  
  @Autowired
  protected IGetThemeConfigurationStrategy getThemeConfigurationStrategy;
  
  @Override
  public IThemeConfigurationModel executeInternal(IModel dataModel) throws Exception
  {
    return getThemeConfigurationStrategy.execute(dataModel);
  }
}
