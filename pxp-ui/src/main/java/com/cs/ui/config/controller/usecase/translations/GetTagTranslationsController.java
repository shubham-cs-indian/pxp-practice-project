package com.cs.ui.config.controller.usecase.translations;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.translations.GetTagTranslationsRequestModel;
import com.cs.core.config.interactor.usecase.translations.IGetTagTranslations;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetTagTranslationsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetTagTranslations getTagTranslations;
  
  @RequestMapping(value = "/dynamictranslations/tag/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetTagTranslationsRequestModel model) throws Exception
  {
    return createResponse(getTagTranslations.execute(model));
  }
}
