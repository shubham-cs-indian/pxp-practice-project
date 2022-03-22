package com.cs.ui.config.controller.usecase.endpoint;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.endpoint.GetAllEndpointsByTypeRequestModel;
import com.cs.di.config.interactor.endpoint.IGetAllEndpointsByType;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllEndpointsByTypeController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllEndpointsByType getAllEndpointsByType;
  
  @RequestMapping(value = "/endpointsbytype", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetAllEndpointsByTypeRequestModel model) throws Exception
  {
    return createResponse(getAllEndpointsByType.execute(model));
  }
}
