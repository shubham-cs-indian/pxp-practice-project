package com.cs.ui.config.controller.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.auditlog.GetGridAuditLogExportRequestModel;
import com.cs.core.config.interactor.usecase.auditlog.IGetGridAuditLogExportTracker;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value="/config")
public class GetGridAuditLogExportController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetGridAuditLogExportTracker getGridAuditLogExportTracker;
  
  @RequestMapping(value = "/auditLog/export", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetGridAuditLogExportRequestModel filterData) throws Exception
  {
    return createResponse(getGridAuditLogExportTracker.execute(filterData));
  }
}
