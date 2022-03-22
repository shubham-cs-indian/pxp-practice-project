package com.cs.ui.runtime.controller.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstancesBasedOnTaskModel;
import com.cs.core.runtime.interactor.usecase.instance.IGetInstancesBasedOnTask;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetInstancesBasedOnTasksController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetInstancesBasedOnTask getInstancesBasedOnTask;
  
  @RequestMapping(value = "/instancebasedontask", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetInstancesBasedOnTaskModel model) throws Exception
  {
    
    return createResponse(getInstancesBasedOnTask.execute(model));
  }
}
