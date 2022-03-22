package com.cs.ui.config.controller.usecase.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.user.GetRolesOrUsersDataByPartnerIdModel;
import com.cs.core.config.interactor.usecase.configdata.IGetRolesOrUsersDataByPartnerId;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetRolesOrUsersDataByPartnerIdController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetRolesOrUsersDataByPartnerId getRolesOrUsersDataByPartnerId;
  
  @RequestMapping(value = "/rolesconfigdata", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetRolesOrUsersDataByPartnerIdModel model) throws Exception
  {
    return createResponse(getRolesOrUsersDataByPartnerId.execute(model));
  }
}
