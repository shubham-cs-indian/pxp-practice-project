package com.cs.ui.config.controller.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.interactor.authorization.IGetPartnerAuthorization;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetPartnerAuthorizationController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetPartnerAuthorization getAuthorizationMapping;
  
  @RequestMapping(value = "/authorizationmappings/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id) throws Exception
  {
    return createResponse(getAuthorizationMapping.execute(new IdParameterModel(id)));
  }
}
