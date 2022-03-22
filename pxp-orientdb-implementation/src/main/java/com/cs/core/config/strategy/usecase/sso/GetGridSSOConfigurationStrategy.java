package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.interactor.model.sso.GetGridSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetGridSSOConfigurationStrategy extends OrientDBBaseStrategy
    implements IGetGridSSOConfigurationStrategy {
  
  @Override
  public IGetGridSSOConfigurationResponseModel execute(IGetGridSSOConfigurationRequestModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ssoSetting", model);
    return execute(GET_GRID_SSO_CONFIGURATION, requestMap,
        GetGridSSOConfigurationResponseModel.class);
  }
}
