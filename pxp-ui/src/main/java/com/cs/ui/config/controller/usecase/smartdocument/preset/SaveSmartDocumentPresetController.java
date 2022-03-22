package com.cs.ui.config.controller.usecase.smartdocument.preset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.smartdocument.preset.SaveSmartDocumentPresetModel;
import com.cs.core.config.interactor.usecase.smartdocument.preset.ISaveSmartDocumentPreset;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveSmartDocumentPresetController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveSmartDocumentPreset saveSmartDocumentPreset;
  
  @RequestMapping(value = "/smartdocumentpreset", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveSmartDocumentPresetModel ruleListModel)
      throws Exception
  {
    return createResponse(saveSmartDocumentPreset.execute(ruleListModel));
  }
}
