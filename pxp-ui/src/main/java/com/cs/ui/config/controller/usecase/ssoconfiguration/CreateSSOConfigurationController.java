package com.cs.ui.config.controller.usecase.ssoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.sso.CreateSSOConfigurationModel;
import com.cs.core.config.interactor.usecase.sso.ICreateSSOConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CreateSSOConfigurationController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateSSOConfiguration createSSOConfiguration;
  
  @RequestMapping(value = "/sso", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateSSOConfigurationModel ssoModel) throws Exception
  {
    return createResponse(createSSOConfiguration.execute(ssoModel));
  }
  
}
