package com.cs.ui.config.controller.usecase.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.template.SaveTemplatePermissionModel;
import com.cs.core.config.interactor.usecase.template.ISaveTemplatePermission;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveTemplatePermissionController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveTemplatePermission saveTemplatePermission;
  
  @RequestMapping(value = "/template/savepermission", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveTemplatePermissionModel requestModel) throws Exception
  {
    return createResponse(saveTemplatePermission.execute(requestModel));
  }
}
