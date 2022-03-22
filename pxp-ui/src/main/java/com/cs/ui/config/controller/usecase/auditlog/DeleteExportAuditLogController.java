package com.cs.ui.config.controller.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.auditlog.IDeleteAuditLogExport;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class DeleteExportAuditLogController extends BaseController implements IConfigController {
  
  @Autowired
  protected IDeleteAuditLogExport deleteAuditLogExport;
  
  @RequestMapping(value = "/auditLog/export", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel ids) throws Exception
  {
    return createResponse(deleteAuditLogExport.execute(ids));
  }
}
