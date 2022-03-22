package com.cs.ui.config.controller.usecase.permission;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.permission.GetPermissionRequestModel;
import com.cs.core.config.interactor.usecase.permission.IGetPermission;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetPermissionController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetPermission getPermission;
  
  @RequestMapping(value = "/permission/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetPermissionRequestModel requestModel) throws Exception
  {
    return createResponse(getPermission.execute(requestModel));
  }
}
