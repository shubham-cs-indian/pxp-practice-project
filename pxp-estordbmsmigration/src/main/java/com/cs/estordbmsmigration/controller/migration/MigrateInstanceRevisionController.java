package com.cs.estordbmsmigration.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.api.estordbmsmigration.interactor.migration.IMigrateRevisions;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

/**
 * Description : Migrate revision and archive revisions to RDBMS from elastic
 * 
 * @author Niraj.Dighe@contentserv.com
 *
 */
@RestController
public class MigrateInstanceRevisionController{
  
  @Autowired
  protected IMigrateRevisions migrateRevisions;
  
  @RequestMapping(value = "/migraterevisions", method = RequestMethod.GET)
  public IVoidModel execute() throws Exception
  {
    return migrateRevisions.execute(new VoidModel());
  }
}
