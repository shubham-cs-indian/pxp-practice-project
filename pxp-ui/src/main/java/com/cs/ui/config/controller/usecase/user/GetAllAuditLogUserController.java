package com.cs.ui.config.controller.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.auditlog.GetGridAuditLogUserRequestModel;
import com.cs.core.config.strategy.usecase.user.IGetAllAuditLogUser;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetAllAuditLogUserController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllAuditLogUser getAllAuditLogUser;
  
  @RequestMapping(value = "/auditlog/username", method = RequestMethod.POST)
  public IRESTModel execute(
      @RequestBody GetGridAuditLogUserRequestModel getGridAuditLogUserRequestModel) throws Exception
  {
    return createResponse(getAllAuditLogUser.execute(getGridAuditLogUserRequestModel));
  }
}
