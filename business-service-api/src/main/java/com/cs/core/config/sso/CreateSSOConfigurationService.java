package com.cs.core.config.sso;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.sso.ICreateSSOConfigurationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSSOConfigurationService extends AbstractCreateConfigService<ICreateSSOConfigurationModel, ICreateSSOConfigurationResponseModel>
    implements ICreateSSOConfigurationService {
  
  @Autowired
  protected ICreateSSOConfigurationStrategy createSSOConfigurationStrategy;
  
  @Autowired
  protected SSOConfigurationValidations     ssoConfigurationValidations;
  
  @Override
  public ICreateSSOConfigurationResponseModel executeInternal(ICreateSSOConfigurationModel dataModel) throws Exception
  {
    ssoConfigurationValidations.validate(dataModel);
    return createSSOConfigurationStrategy.execute(dataModel);
  }
}
