package com.cs.ui.config.controller.usecase.globalpermissions;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionWithAllowedTemplatesRequestModel;
import com.cs.core.config.interactor.usecase.globalpermissions.IGetGlobalPermissionWithAllowedTemplates;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetGlobalPermissionWithAllowedTemplatesController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetGlobalPermissionWithAllowedTemplates getGlobalPermissionWithAllowedTemplates;
  
  @RequestMapping(value = "/globalpermissionwithallowedtemplates", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetGlobalPermissionWithAllowedTemplatesRequestModel model)
      throws Exception
  {
    
    return createResponse(getGlobalPermissionWithAllowedTemplates.execute(model));
  }
}
