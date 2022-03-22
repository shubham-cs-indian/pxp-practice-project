package com.cs.ui.config.controller.usecase.ssoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.sso.GetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.usecase.sso.IGetGridSSOConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetGridSSOConfigurationController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetGridSSOConfiguration getGridSSOConfiguation;
  
  @RequestMapping(value = "/sso/grid", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetGridSSOConfigurationRequestModel ssoModel) throws Exception
  {
    return createResponse(getGridSSOConfiguation.execute(ssoModel));
  }
}
