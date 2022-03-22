package com.cs.ui.config.controller.usecase.smartdocument.preset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.smartdocument.preset.SmartDocumentPresetModel;
import com.cs.core.config.interactor.usecase.smartdocument.preset.ICreateSmartDocumentPreset;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateSmartDocumentPresetController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ICreateSmartDocumentPreset createSmartDocumentPreset;
  
  @RequestMapping(value = "/smartdocumentpreset", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody SmartDocumentPresetModel ruleList) throws Exception
  {
    return createResponse(createSmartDocumentPreset.execute(ruleList));
  }
}
