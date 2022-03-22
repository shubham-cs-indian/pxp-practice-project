package com.cs.ui.common.controller.usecase.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.runtime.interactor.initiateexport.IInitiateExport;
import com.cs.di.runtime.model.initiateexport.ExportDataModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/export")
public class InitiateExportController extends BaseController implements IConfigController {
  
  @Autowired
  protected IInitiateExport initiateExport;
  
  @RequestMapping(value = "/excel", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ExportDataModel exportModel) throws Exception
  {
    return createResponse(initiateExport.execute(exportModel));
  }
}
