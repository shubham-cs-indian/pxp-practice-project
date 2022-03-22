package com.cs.ui.config.controller.usecase.endpoint;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.endpoint.EndpointModel;
import com.cs.di.config.interactor.endpoint.ICreateEndpoint;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateEndpointController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateEndpoint createEndpoint;
  
  @RequestMapping(value = "/endpoints", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody EndpointModel endpointModel) throws Exception
  {
    return createResponse(createEndpoint.execute(endpointModel));
  }
}
