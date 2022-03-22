package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IMigrateDownloadInformation;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

/**
 * Description : Controller for migrating download information for download tracker
 * 
 * @author mrunali.dhenge
 *
 */
@RestController
public class MigrateDownloadInformationController{
  
  @Autowired
  IMigrateDownloadInformation migrateDownloadInformation;
  
  @RequestMapping(value = "/migratedownloadinformation", method = RequestMethod.GET)
  public IVoidModel execute() throws Exception
  {
    return migrateDownloadInformation.execute(new VoidModel());
  }
}
