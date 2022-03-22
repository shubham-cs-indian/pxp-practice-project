package com.cs.core.config.interactor.usecase.sso;

import com.cs.core.config.sso.ICreateSSOConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.sso.ICreateSSOConfigurationStrategy;

@Service
public class CreateSSOConfiguration extends
    AbstractCreateConfigInteractor<ICreateSSOConfigurationModel, ICreateSSOConfigurationResponseModel>
    implements ICreateSSOConfiguration {
  
  @Autowired
  protected ICreateSSOConfigurationService createSSOConfigurationService;
  
  @Override
  public ICreateSSOConfigurationResponseModel executeInternal(ICreateSSOConfigurationModel dataModel) throws Exception
  {
    return createSSOConfigurationService.execute(dataModel);
  }
}
