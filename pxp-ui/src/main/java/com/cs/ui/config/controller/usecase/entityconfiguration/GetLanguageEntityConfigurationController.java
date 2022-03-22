package com.cs.ui.config.controller.usecase.entityconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.usecase.entityconfiguration.IGetEntityConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetLanguageEntityConfigurationController extends BaseController implements IConfigController {

  @Autowired
  protected IGetEntityConfiguration getLanguageEntityConfiguration;

  @RequestMapping(value = "/getentityconfig/language", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetEntityConfigurationRequestModel model) throws Exception {
    return createResponse(getLanguageEntityConfiguration.execute(model));
  }

}
