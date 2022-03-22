package com.cs.ui.config.controller.usecase.language;

import com.cs.core.config.interactor.model.language.DeleteLanguageRequestModel;
import com.cs.core.config.interactor.usecase.language.IDeleteLanguage;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class DeleteLanguageController extends BaseController {
  
  @Autowired
  protected IDeleteLanguage deleteLanguage;
  
  @RequestMapping(value = "/languages", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteLanguageRequestModel model) throws Exception
  {
    return createResponse(deleteLanguage.execute(model));
  }
}
