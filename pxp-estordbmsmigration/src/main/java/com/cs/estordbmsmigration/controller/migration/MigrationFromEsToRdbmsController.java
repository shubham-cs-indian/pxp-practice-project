package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IMigrationFromEsToRdbms;
import com.cs.api.estordbmsmigration.model.migration.MigrationFromEsToRdbmsRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@RestController
public class MigrationFromEsToRdbmsController {
  
  @Autowired
  protected IMigrationFromEsToRdbms migrationFromEsToRdbms;
  
  @RequestMapping(value = "/migrationfromestordbms", method = RequestMethod.POST)
  public IVoidModel execute(@RequestBody MigrationFromEsToRdbmsRequestModel model) throws Exception
  {
    return migrationFromEsToRdbms.execute(model);
  }
}
