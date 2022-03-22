package com.cs.ui.runtime.controller.usecase.translations;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.runtime.interactor.model.translations.GetStaticTranslationsForRuntimeRequestModel;
import com.cs.core.runtime.interactor.usecase.translations.IGetStaticTranslationsForRuntime;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetStaticTranslationsForRuntimeController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetStaticTranslationsForRuntime getStaticTranslationsForRuntime;
  
  @RequestMapping(value = "/statictranslations/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetStaticTranslationsForRuntimeRequestModel model)
      throws Exception
  {
    return createResponse(getStaticTranslationsForRuntime.execute(model));
  }
}
