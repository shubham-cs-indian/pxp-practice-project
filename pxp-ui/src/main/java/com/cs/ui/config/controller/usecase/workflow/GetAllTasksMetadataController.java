package com.cs.ui.config.controller.usecase.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.config.model.modeler.WorkflowTaskRequestModel;
import com.cs.di.config.interactor.modeler.IGetAllTasksMetadata;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetAllTasksMetadataController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllTasksMetadata getAllTasksMetadata;
  
  @RequestMapping(value = "/alltasks", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody WorkflowTaskRequestModel requestModel) throws Exception
  {
    return createResponse(getAllTasksMetadata.execute(requestModel));
  }
}
