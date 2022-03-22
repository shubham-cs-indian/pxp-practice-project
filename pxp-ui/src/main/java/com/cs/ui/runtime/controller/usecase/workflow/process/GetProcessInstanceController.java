package com.cs.ui.runtime.controller.usecase.workflow.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.workflow.processinstance.IGetBGPProcessInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetProcessInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetBGPProcessInstance getProcessInstance;
  
  @RequestMapping(value = "/procesinstance/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id) throws Exception
  {
    return createResponse(getProcessInstance.execute(new IdParameterModel(id)));
  }
}
