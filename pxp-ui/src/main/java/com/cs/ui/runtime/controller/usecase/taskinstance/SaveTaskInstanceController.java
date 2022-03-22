package com.cs.ui.runtime.controller.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.taskinstance.SaveTaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.taskinstance.ISaveTaskInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveTaskInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  ISaveTaskInstance saveTaskInstance;
  
  @RequestMapping(value = "/taskinstances", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveTaskInstanceModel model) throws Exception
  {
    return createResponse(saveTaskInstance.execute(model));
  }
}
