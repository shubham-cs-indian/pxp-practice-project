package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IMigrateAuditLogInformation;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@RestController
public class MigrateAuditLogInformationController {
  
  @Autowired
  IMigrateAuditLogInformation migrateAuditLogInformation;
  
  @RequestMapping(value = "/migrateAuditLogInformation", method = RequestMethod.GET)
  public IVoidModel execute() throws Exception
  {
    return migrateAuditLogInformation.execute(new VoidModel());
  }
}
