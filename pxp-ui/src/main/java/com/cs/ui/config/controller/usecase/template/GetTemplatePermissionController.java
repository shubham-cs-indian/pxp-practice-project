package com.cs.ui.config.controller.usecase.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.template.PermissionRequestModel;
import com.cs.core.config.interactor.usecase.template.IGetTemplatePermission;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetTemplatePermissionController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetTemplatePermission getTemplatePermission;
  
  @RequestMapping(value = "/template/permission", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody PermissionRequestModel requestModel) throws Exception
  {
    return createResponse(getTemplatePermission.execute(requestModel));
  }
}
