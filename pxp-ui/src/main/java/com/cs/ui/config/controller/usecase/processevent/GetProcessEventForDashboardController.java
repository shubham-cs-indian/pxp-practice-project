package com.cs.ui.config.controller.usecase.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.di.config.interactor.processevent.IGetProcessEventForDashboard;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetProcessEventForDashboardController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetProcessEventForDashboard getProcessEventForDashboard;
  
  @RequestMapping(value = "/processevents/dashboard", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel getModel) throws Exception
  {
    return createResponse(getProcessEventForDashboard.execute(getModel));
  }
}
