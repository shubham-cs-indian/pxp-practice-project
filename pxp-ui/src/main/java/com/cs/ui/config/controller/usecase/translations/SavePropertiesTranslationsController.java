package com.cs.ui.config.controller.usecase.translations;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.translations.AbstractSaveTranslationsRequestModel;
import com.cs.core.config.interactor.usecase.translations.ISavePropertiesTranslations;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SavePropertiesTranslationsController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ISavePropertiesTranslations savePropertiesTranslations;
  
  @RequestMapping(value = "/dynamictranslations/save", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AbstractSaveTranslationsRequestModel model)
      throws Exception
  {
    return createResponse(savePropertiesTranslations.execute(model));
  }
}
