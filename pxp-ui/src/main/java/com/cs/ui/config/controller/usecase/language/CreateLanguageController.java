package com.cs.ui.config.controller.usecase.language;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.language.CreateLanguageModel;
import com.cs.core.config.interactor.usecase.language.ICreateLanguage;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateLanguageController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateLanguage createLanguage;
  
  @RequestMapping(value = "/languages", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateLanguageModel model) throws Exception
  {
    return createResponse(createLanguage.execute(model));
  }
}
