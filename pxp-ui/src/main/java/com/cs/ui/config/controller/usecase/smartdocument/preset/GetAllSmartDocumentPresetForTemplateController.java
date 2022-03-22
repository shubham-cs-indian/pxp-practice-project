package com.cs.ui.config.controller.usecase.smartdocument.preset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.smartdocument.preset.IGetAllSmartDocumentPresetForTemplate;
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
public class GetAllSmartDocumentPresetForTemplateController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAllSmartDocumentPresetForTemplate getAllSmartDocumentPresetForTemplate;
  
  @RequestMapping(value = "/smartdocumentpresetfortemplate/getall", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdParameterModel model) throws Exception
  {
    return createResponse(getAllSmartDocumentPresetForTemplate.execute(model));
  }
}
