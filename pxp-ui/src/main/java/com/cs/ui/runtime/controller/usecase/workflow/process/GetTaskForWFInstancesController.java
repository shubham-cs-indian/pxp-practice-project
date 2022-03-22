package com.cs.ui.runtime.controller.usecase.workflow.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.di.runtime.model.processinstance.GetProcessInstanceModel;
import com.cs.di.runtime.strategy.processinstance.IGetTaskForWFInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTaskForWFInstancesController extends BaseController implements IRuntimeController {
  
  @Autowired IGetTaskForWFInstances getWorkflowInstances;
  
  @RequestMapping(value = "/procesinstances/task", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetProcessInstanceModel requestModel) throws Exception
  {
    return createResponse(getWorkflowInstances.execute(requestModel));
  }
}
