package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.migration.IGoldenRecordBaseEntityMigrationService;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping
public class GoldenRecordBaseEntityMigrationController  extends BaseController {
  
  @Autowired
  protected IGoldenRecordBaseEntityMigrationService goldenRecordBaseEntityMigrationService;
  
  @RequestMapping(value = "/goldenrecordbaseentitymigration", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(goldenRecordBaseEntityMigrationService.execute(new VoidModel()));
  }
}