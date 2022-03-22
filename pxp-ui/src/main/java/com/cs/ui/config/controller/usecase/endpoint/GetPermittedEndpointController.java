package com.cs.ui.config.controller.usecase.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.GetConfigDataRequestModel;
import com.cs.core.config.interactor.usecase.endpoint.IGetPermittedEndpoint;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetPermittedEndpointController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetPermittedEndpoint getPermittedEndpoint;
  
  @RequestMapping(value = "/permitted/endpoint", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetConfigDataRequestModel model) throws Exception
  {
    return createResponse(getPermittedEndpoint.execute(model));
  }
}
