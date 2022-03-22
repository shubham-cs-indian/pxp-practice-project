package com.cs.ui.config.controller.usecase.task;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.usecase.task.ICreateTask;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateTaskController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateTask createTask;
  
  @RequestMapping(value = "/tasks", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody TaskModel taskModel) throws Exception
  {
    return createResponse(createTask.execute(taskModel));
  }
}
