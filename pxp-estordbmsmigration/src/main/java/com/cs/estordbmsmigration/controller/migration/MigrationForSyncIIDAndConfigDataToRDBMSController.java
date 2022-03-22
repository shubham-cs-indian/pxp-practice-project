package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.ISyncConfigToRDBMS;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@RestController
public class MigrationForSyncIIDAndConfigDataToRDBMSController {
  
  @Autowired
  protected ISyncConfigToRDBMS syncConfigToRDBMS;
  
  @RequestMapping(value = "/migrationforsyncconfig", method = RequestMethod.POST)
  public IVoidModel execute() throws Exception
  {
    return syncConfigToRDBMS.execute(new VoidModel());
  }
  
}
