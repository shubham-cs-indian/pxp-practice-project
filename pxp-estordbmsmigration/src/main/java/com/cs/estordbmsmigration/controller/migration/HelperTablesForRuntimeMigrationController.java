package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IHelperTablesForRuntimeMigration;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

/***
 * This controller is responsible for calling the service which will insert
 * config data into helper tables for runtime migration.
 * 
 * @author vannya.kalani
 *
 */
@RestController
public class HelperTablesForRuntimeMigrationController {
  
  @Autowired
  protected IHelperTablesForRuntimeMigration helperTablesForRuntimeMigration;
  
  @RequestMapping(value = "/migrationhelpertables", method = RequestMethod.POST)
  public IVoidModel execute() throws Exception
  {
    return helperTablesForRuntimeMigration.execute(new VoidModel());
  }
  
}
