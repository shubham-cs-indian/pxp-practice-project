package com.cs.ui.config.controller.usecase.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.processevent.ProcessEventModel;
import com.cs.di.config.interactor.processevent.ICreateProcessEvent;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CreateProcessEventController extends BaseController implements IConfigController {

  @Autowired protected ICreateProcessEvent createProcessEvent;

  @RequestMapping(value = "/processevents", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody ProcessEventModel processEventModel) throws Exception {
    return createResponse(createProcessEvent.execute(processEventModel));
  }
}
