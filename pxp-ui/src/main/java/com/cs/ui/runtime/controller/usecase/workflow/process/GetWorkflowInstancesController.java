
package com.cs.ui.runtime.controller.usecase.workflow.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.di.runtime.model.processinstance.GetProcessInstanceModel;
import com.cs.di.runtime.strategy.processinstance.IGetWorkflowInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetWorkflowInstancesController extends BaseController implements IRuntimeController {
  
  @Autowired IGetWorkflowInstances getWorkflowInstances;
  
  @RequestMapping(value = "/procesinstances/search", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetProcessInstanceModel requestModel) throws Exception
  {
    return createResponse(getWorkflowInstances.execute(requestModel));
  }
}
