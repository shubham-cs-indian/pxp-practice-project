package com.cs.ui.runtime.controller.usecase.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.usecase.workflow.IGetAllBGPServices;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
@SuppressWarnings("unchecked")
public class GetAllBGPServicesController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetAllBGPServices getAllBGPServices;
  
  @RequestMapping(value = "/bgp/getallservices", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllBGPServices.execute(null));
  }
}