package com.cs.ui.config.controller.usecase.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.usecase.idsserver.IUpdateINDSInstancesData;
import com.cs.dam.config.interactor.model.idsserver.INDSConfigurationTaskRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class UpdateINDSInstancesDataController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IUpdateINDSInstancesData updateINDSInstancesData;
  
  @RequestMapping(value = "indesignserver/instances/update", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody INDSConfigurationTaskRequestModel requestModel)
      throws Exception
  {
    return createResponse(updateINDSInstancesData.execute(requestModel));
  }
}
