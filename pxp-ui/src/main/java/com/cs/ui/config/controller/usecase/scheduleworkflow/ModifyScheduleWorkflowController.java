package com.cs.ui.config.controller.usecase.scheduleworkflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.config.interactor.model.initializeworflowevent.SaveProcessEventModel;
import com.cs.di.config.interactor.scheduleworkflow.IModifyScheduleWorkflow;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * @author sangram.shelar
 *
 */
@RestController
@RequestMapping(value = "/config")
public class ModifyScheduleWorkflowController extends BaseController implements IConfigController {
  
  @Autowired
  IModifyScheduleWorkflow modifyScheduleWorkflow;
  
  @RequestMapping(value = "/scheduleworkflow", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveProcessEventModel processEventModel) throws Exception
  {
    return createResponse(modifyScheduleWorkflow.execute(processEventModel));
  }
}
