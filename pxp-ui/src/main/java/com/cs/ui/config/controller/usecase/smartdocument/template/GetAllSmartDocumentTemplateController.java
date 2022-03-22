package com.cs.ui.config.controller.usecase.smartdocument.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.smartdocument.template.IGetAllSmartDocumentTemplate;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllSmartDocumentTemplateController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAllSmartDocumentTemplate getAllSmartDocumentTemplate;
  
  @RequestMapping(value = "/smartDocumentTemplate", method = RequestMethod.GET)
  public IRESTModel execute(@RequestBody IdParameterModel model) throws Exception
  {
    return createResponse(getAllSmartDocumentTemplate.execute(model));
  }
}
