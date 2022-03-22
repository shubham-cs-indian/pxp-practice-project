package com.cs.ui.config.controller.usecase.smartdocument;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.smartdocument.SmartDocumentModel;
import com.cs.core.config.interactor.usecase.smartdocument.ISaveSmartDocument;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveSmartDocumentController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveSmartDocument saveSmartDocument;
  
  @RequestMapping(value = "/smartdocument", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SmartDocumentModel ruleListModel) throws Exception
  {
    return createResponse(saveSmartDocument.execute(ruleListModel));
  }
}
