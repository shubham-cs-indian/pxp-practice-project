package com.cs.ui.config.controller.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.config.interactor.authorization.ICreatePartnerAuthorization;
import com.cs.di.config.model.authorization.PartnerAuthorizationModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CreatePartnerAuthorizationController extends BaseController implements IConfigController {

  @Autowired
  protected ICreatePartnerAuthorization createAuthorizationMapping;
  
  @RequestMapping(value = "/authorizationmappings", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody PartnerAuthorizationModel authorizationModel) throws Exception
  {
    return createResponse(createAuthorizationMapping.execute(authorizationModel));
  }
}