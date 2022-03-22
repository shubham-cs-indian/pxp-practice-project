package com.cs.ui.config.controller.usecase.pdfreactorserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.smartdocument.SmartDocumentModel;
import com.cs.core.config.interactor.usecase.pdfreactorserver.ICheckAndSavePdfReactorServerConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is called to save PDF reactor configuration.
 * @author pranav.huchche
 */
@RestController
@RequestMapping(value = "/config")
public class SavePdfReactorServerConfigurationController extends BaseController
    implements IConfigController {
  
  @Autowired
  ICheckAndSavePdfReactorServerConfiguration checkAndSavePdfReactorServerConfiguration;
  
  @RequestMapping(value = "/pdfReactorServer", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SmartDocumentModel requestModel)
      throws Exception
  {    
    return createResponse(checkAndSavePdfReactorServerConfiguration.execute(requestModel));
  }
  
}
