package com.cs.core.config.interactor.usecase.sso;

import com.cs.core.config.sso.IGetGridSSOConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.sso.IGetGridSSOConfigurationStrategy;

@Component
public class GetGridSSOConfiguration extends
    AbstractGetConfigInteractor<IGetGridSSOConfigurationRequestModel, IGetGridSSOConfigurationResponseModel>
    implements IGetGridSSOConfiguration {
  
  @Autowired
  protected IGetGridSSOConfigurationService getGridSSOConfigurationService;
  
  public IGetGridSSOConfigurationResponseModel executeInternal(
      IGetGridSSOConfigurationRequestModel dataModel) throws Exception
  {
    return getGridSSOConfigurationService.execute(dataModel);
  }
}
