package com.cs.core.config.strategy.usecase.sso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.sso.CreateSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class CreateSSOConfigurationStrategy extends OrientDBBaseStrategy
    implements ICreateSSOConfigurationStrategy {
  
  @Override
  public ICreateSSOConfigurationResponseModel execute(ICreateSSOConfigurationModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ssoSetting", model);
    return execute(CREATE_SSO_CONFIGURATION, requestMap, CreateSSOConfigurationResponseModel.class);
  }
}
