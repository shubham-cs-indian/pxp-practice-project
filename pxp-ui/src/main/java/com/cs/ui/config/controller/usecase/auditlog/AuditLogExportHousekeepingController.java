package com.cs.ui.config.controller.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.auditlog.HouseKeepingRequestModel;
import com.cs.core.config.interactor.usecase.auditlog.IAuditLogExportHouseKeeping;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class AuditLogExportHousekeepingController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IAuditLogExportHouseKeeping auditLogExportHouseKeeping;
  
  @RequestMapping(value = "/auditLog/export/houseKeeping", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody HouseKeepingRequestModel model) throws Exception
  {
    return createResponse(auditLogExportHouseKeeping.execute(model));
  }
}
