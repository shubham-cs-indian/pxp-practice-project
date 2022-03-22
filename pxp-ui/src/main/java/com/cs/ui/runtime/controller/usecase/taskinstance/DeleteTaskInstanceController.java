package com.cs.ui.runtime.controller.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.taskinstance.DeleteTaskInstancesRequestModel;
import com.cs.core.runtime.interactor.usecase.taskinstance.IDeleteTaskInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class DeleteTaskInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IDeleteTaskInstance deleteTaskInstance;
  
  @RequestMapping(value = "/taskinstances", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteTaskInstancesRequestModel ideleteModel) throws Exception
  {
    return createResponse(deleteTaskInstance.execute(ideleteModel));
  }
}
