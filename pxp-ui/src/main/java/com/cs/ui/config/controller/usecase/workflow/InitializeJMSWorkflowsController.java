package com.cs.ui.config.controller.usecase.workflow;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.jms.IJMSListenerInitializationService;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(value = "/runtime")
@SuppressWarnings("unchecked")
public class InitializeJMSWorkflowsController extends BaseController implements IConfigController {
  
  @Autowired
  IJMSListenerInitializationService jmsListenerInitializationService;
  
  @RequestMapping(value = "/jms/initialize", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(jmsListenerInitializationService.execute(null));
  }

  @PostConstruct
  public void init() throws Exception
  {
    jmsListenerInitializationService.execute(null);
  }
}