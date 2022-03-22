package com.cs.ui.config.controller.usecase.endpoint;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.di.config.interactor.endpoint.IGetGridEndpoints;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetGridEndpointsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetGridEndpoints getGridEndpoints;
  
  @RequestMapping(value = "/endpoints/grid", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel getAllModel) throws Exception
  {
    return createResponse(getGridEndpoints.execute(getAllModel));
  }
}
