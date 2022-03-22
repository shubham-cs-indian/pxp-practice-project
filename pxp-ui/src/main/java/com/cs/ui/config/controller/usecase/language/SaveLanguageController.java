package com.cs.ui.config.controller.usecase.language;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.language.LanguageModel;
import com.cs.core.config.interactor.usecase.language.ISaveLanguage;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveLanguageController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveLanguage saveLanguage;
  
  @RequestMapping(value = "/languages", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody LanguageModel model) throws Exception
  {
    return createResponse(saveLanguage.execute(model));
  }
}
