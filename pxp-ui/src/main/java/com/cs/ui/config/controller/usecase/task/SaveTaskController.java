package com.cs.ui.config.controller.usecase.task;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.usecase.task.ISaveTask;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/config")
public class SaveTaskController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveTask saveTask;
  
  @RequestMapping(value = "/tasks", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<TaskModel> taskModel) throws Exception
  {
    IListModel<ITaskModel> tasksListSaveModel = new ListModel<>();
    tasksListSaveModel.setList(taskModel);
    return createResponse(saveTask.execute(tasksListSaveModel));
  }
}
