package com.cs.ui.config.controller.usecase.role;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.role.RoleSaveModel;
import com.cs.core.config.interactor.usecase.role.ISaveRole;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveRoleController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveRole saveRole;
  
  @RequestMapping(value = "/roles", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody RoleSaveModel roleModel) throws Exception
  {
    return createResponse(saveRole.execute(roleModel));
  }
}
