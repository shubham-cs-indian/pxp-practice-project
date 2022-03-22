package com.cs.ui.config.controller.usecase.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.config.interactor.model.initializeworflowevent.SaveProcessEventModel;
import com.cs.di.config.interactor.processevent.ISaveProcessEvent;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class SaveProcessEventController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveProcessEvent saveProcessEvent;
  
  @RequestMapping(value = "/processevents", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveProcessEventModel processEventModel) throws Exception
  {
    return createResponse(saveProcessEvent.execute(processEventModel));
  }
}
