package com.cs.ui.config.controller.usecase.organization;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.organization.SaveOrganizationModel;
import com.cs.core.config.interactor.usecase.organization.ISaveOrganization;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveOrganizationController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveOrganization saveOrganization;
  
  @RequestMapping(value = "/organizations", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveOrganizationModel saveOrganizationModel)
      throws Exception
  {
    return createResponse(saveOrganization.execute(saveOrganizationModel));
  }
}
