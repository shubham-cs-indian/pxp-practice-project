package com.cs.ui.config.controller.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.config.interactor.authorization.ISavePartnerAuthorization;
import com.cs.di.config.model.authorization.SavePartnerAuthorizationModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class SavePartnerAuthorizationController extends BaseController implements IConfigController {

  @Autowired
  protected ISavePartnerAuthorization saveAuthorizationMapping;
  
  @RequestMapping(value = "/authorizationmappings", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SavePartnerAuthorizationModel saveAuthorizationMappingModel) throws Exception
  {
    return createResponse(saveAuthorizationMapping.execute(saveAuthorizationMappingModel));
  }
}
