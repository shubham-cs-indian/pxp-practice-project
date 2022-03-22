package com.cs.ui.config.controller.usecase.role;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.role.CreateRoleModel;
import com.cs.core.config.interactor.usecase.role.ICreateRole;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateRoleController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateRole createRole;
  
  @RequestMapping(value = "/roles", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateRoleModel roleModel) throws Exception
  {
    return createResponse(createRole.execute(roleModel));
  }
}
