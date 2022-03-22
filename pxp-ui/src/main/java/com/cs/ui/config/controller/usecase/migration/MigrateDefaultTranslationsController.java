package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IMigrateDefaultTranslations;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping
public class MigrateDefaultTranslationsController extends BaseController {
  
  @Autowired
  IMigrateDefaultTranslations migrateDefaultTranslations;
  
  @RequestMapping(value = "/migratedefaulttranslations", method = RequestMethod.GET)
  public IRESTModel execute(@RequestParam(value = "languageCode") String languageCode) throws Exception
  {
    IIdParameterModel requestModel = new IdParameterModel();
    requestModel.setId(languageCode);
    return createResponse(migrateDefaultTranslations.execute(requestModel));
  }
}
