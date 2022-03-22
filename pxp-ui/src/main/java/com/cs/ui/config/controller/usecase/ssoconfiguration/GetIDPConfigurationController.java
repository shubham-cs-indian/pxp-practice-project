package com.cs.ui.config.controller.usecase.ssoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.sso.IGetIDPConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetIDPConfigurationController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetIDPConfiguration getIDPConfiguration;
  
  @RequestMapping(value = "/idpConfiguration", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getIDPConfiguration.execute(null));
  }
}
