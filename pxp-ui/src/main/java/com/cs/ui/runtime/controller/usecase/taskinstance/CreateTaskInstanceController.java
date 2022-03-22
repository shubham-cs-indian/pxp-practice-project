package com.cs.ui.runtime.controller.usecase.taskinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.taskinstance.TaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.taskinstance.ICreateTaskInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class CreateTaskInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ICreateTaskInstance createTaskInstance;
  
  @RequestMapping(value = "/taskinstances", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody TaskInstanceModel taskInstanceModel) throws Exception
  {
    return createResponse(createTaskInstance.execute(taskInstanceModel));
  }
}
