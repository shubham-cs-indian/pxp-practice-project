package com.cs.ui.config.controller.usecase.task;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.task.IGetAllTask;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllTasksController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllTask getAllTask;
  
  @RequestMapping(value = "/tasks/getall", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    IIdParameterModel dataModel = new IdParameterModel("");
    return createResponse(getAllTask.execute(dataModel));
  }
}
