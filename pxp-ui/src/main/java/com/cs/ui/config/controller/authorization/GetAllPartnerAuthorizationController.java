package com.cs.ui.config.controller.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.di.config.interactor.authorization.IGetAllPartnerAuthorization;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetAllPartnerAuthorizationController extends BaseController implements IConfigController {

  @Autowired
  protected IGetAllPartnerAuthorization getAllAuthorizationMappings;
  
  @RequestMapping(value = "/authorizationmappings/getall", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel model) throws Exception
  {
    return createResponse(getAllAuthorizationMappings.execute(model));
  }
}
