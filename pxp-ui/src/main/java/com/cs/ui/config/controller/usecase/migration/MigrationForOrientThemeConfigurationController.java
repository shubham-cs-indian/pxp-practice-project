package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IMigrationForThemeConfiguration;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class MigrationForOrientThemeConfigurationController extends BaseController {
	  
	  @Autowired
	  IMigrationForThemeConfiguration migrationForThemeConfiguration;
	  
	  @RequestMapping(value = "/migrationforthemeconfiguration", method = RequestMethod.GET)
	  public IRESTModel execute() throws Exception
	  {
	    return createResponse(migrationForThemeConfiguration.execute(new VoidModel()));
	  }
	}
