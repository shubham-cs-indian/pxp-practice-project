package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IMigrationForThemeConfiguration19_1SR;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
/*@RequestMapping(value = "/config")*/
public class MigrationForOrientThemeConfiguration19_1SRController extends BaseController {
  
  @Autowired
  IMigrationForThemeConfiguration19_1SR migrationForThemeConfiguration19_1sr;
  
  @RequestMapping(value = "/config/migrationforthemeconfiguration19_1SR", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(migrationForThemeConfiguration19_1sr.execute(new VoidModel()));
  }
}
