package com.cs.ui.config.controller.usecase.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.role.CreateRoleCloneModel;
import com.cs.core.config.interactor.usecase.role.ICreateRoleClone;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CreateRoleCloneController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateRoleClone cloneRole;
  
  @RequestMapping(value = "/clonerole", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateRoleCloneModel requestModel) throws Exception
  {
    return createResponse(cloneRole.execute(requestModel));
  }
  
}
