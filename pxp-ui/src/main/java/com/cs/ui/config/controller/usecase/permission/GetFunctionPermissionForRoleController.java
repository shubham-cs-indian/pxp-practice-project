package com.cs.ui.config.controller.usecase.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.permission.IGetFunctionPermissionForRole;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetFunctionPermissionForRoleController extends BaseController implements IConfigController {
  
  @Autowired
  IGetFunctionPermissionForRole getFunctionPermissionForRole;
  
  @RequestMapping(value = "/permission/function/{roleId}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String roleId) throws Exception
  {
    IIdParameterModel model = new IdParameterModel(roleId);
    return createResponse(getFunctionPermissionForRole.execute(model));
  }
  
}
