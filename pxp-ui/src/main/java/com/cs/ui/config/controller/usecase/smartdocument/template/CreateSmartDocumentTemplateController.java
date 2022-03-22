package com.cs.ui.config.controller.usecase.smartdocument.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.smartdocument.template.SmartDocumentTemplateModel;
import com.cs.core.config.interactor.usecase.smartdocument.template.ICreateSmartDocumentTemplate;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateSmartDocumentTemplateController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ICreateSmartDocumentTemplate createSmartDocumentTemplate;
  
  @RequestMapping(value = "/smartDocumentTemplate", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody SmartDocumentTemplateModel templateModel) throws Exception
  {
    return createResponse(createSmartDocumentTemplate.execute(templateModel));
  }
}
