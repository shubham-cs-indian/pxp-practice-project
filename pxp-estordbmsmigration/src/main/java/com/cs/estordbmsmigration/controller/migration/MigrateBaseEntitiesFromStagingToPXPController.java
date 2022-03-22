package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IMigrateFromStagingToPXP;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

/**
 * This controller is responsible for calling the service which will call
 * sp_migrateallentitiesfromestordbms procedure for runtime migration.
 * 
 * @author vannya.kalani
 *
 */
@RestController
public class MigrateBaseEntitiesFromStagingToPXPController {
  
  @Autowired
  protected IMigrateFromStagingToPXP migrateBaseEntitiesFromStagingToPXP;
  
  @RequestMapping(value = "/migrationbaseentities", method = RequestMethod.GET)
  public IVoidModel execute() throws Exception
  {
    return migrateBaseEntitiesFromStagingToPXP.execute(new VoidModel());
  }
  
}
