package com.cs.ui.config.controller.usecase.endpoint;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.endpoint.EndpointModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.di.config.interactor.endpoint.IGetAllEndpoints;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllEndpointsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllEndpoints getAllEndpoints;
  
  @RequestMapping(value = "/endpoints", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    IEndpointModel model = new EndpointModel();
    return createResponse(getAllEndpoints.execute(model));
  }
}
