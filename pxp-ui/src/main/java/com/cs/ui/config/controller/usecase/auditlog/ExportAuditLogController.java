package com.cs.ui.config.controller.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.auditlog.GetGridAuditLogRequestModel;
import com.cs.core.config.interactor.usecase.auditlog.ICreateAuditLogExportTracker;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class ExportAuditLogController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateAuditLogExportTracker createAuditLogExportTracker;
  
  @RequestMapping(value = "/auditLog/export", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody GetGridAuditLogRequestModel requestModel) throws Exception
  {
    return createResponse(createAuditLogExportTracker.execute(requestModel));
  }
}
