package com.cs.ui.config.controller.usecase.language;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.language.GetLanguagesRequestModel;
import com.cs.core.config.interactor.usecase.language.IGetLanguages;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetLanguagesController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetLanguages getLanguages;
  
  @RequestMapping(value = "/languagesinfo", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetLanguagesRequestModel model) throws Exception
  {
    return createResponse(getLanguages.execute(model));
  }
}
