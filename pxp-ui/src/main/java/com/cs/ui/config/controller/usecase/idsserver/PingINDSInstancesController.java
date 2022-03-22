package com.cs.ui.config.controller.usecase.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.usecase.idsserver.IPingINDSInstances;
import com.cs.dam.config.interactor.model.idsserver.INDSPingTaskRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class PingINDSInstancesController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IPingINDSInstances pingINDSInstances;
  
  @RequestMapping(value = "indesignserver/instances/checkstatus", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody INDSPingTaskRequestModel requestModel) throws Exception
  {
    return createResponse(pingINDSInstances.execute(requestModel));
  }
  
}
