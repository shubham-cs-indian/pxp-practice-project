package com.cs.ui.config.controller.usecase.smartdocument.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.smartdocument.template.SmartDocumentTemplateModel;
import com.cs.core.config.interactor.usecase.smartdocument.template.ISaveSmartDocumentTemplate;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveSmartDocumentTemplateController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ISaveSmartDocumentTemplate saveSmartDocumentTemplate;
  
  @RequestMapping(value = "/smartDocumentTemplate", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SmartDocumentTemplateModel templateModel) throws Exception
  {
    return createResponse(saveSmartDocumentTemplate.execute(templateModel));
  }
}
