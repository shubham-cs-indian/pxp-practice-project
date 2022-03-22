package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IMigrateFromStagingToPXP;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@RestController
public class MigrationWorkaroundScriptsController {
  
  @Autowired
  protected IMigrateFromStagingToPXP migrationWorkaroundScripts;
  
  @RequestMapping(value = "/migrationworkaround", method = RequestMethod.GET)
  public IVoidModel execute() throws Exception
  {
    return migrationWorkaroundScripts.execute(new VoidModel());
  }
  
}
