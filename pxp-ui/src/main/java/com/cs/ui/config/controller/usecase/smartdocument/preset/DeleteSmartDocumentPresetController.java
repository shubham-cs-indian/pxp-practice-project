package com.cs.ui.config.controller.usecase.smartdocument.preset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.smartdocument.preset.IDeleteSmartDocumentPreset;
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
public class DeleteSmartDocumentPresetController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IDeleteSmartDocumentPreset deleteSmartDocumentPreset;
  
  @RequestMapping(value = "/smartdocumentpreset", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdParameterModel idParameterModel) throws Exception
  {
    return createResponse(deleteSmartDocumentPreset.execute(idParameterModel));
  }
}
