package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.usecase.migration.IMigrateDeprecateVirtualCatalog;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * Controller to delete virtual catalog standard classes from orient and
 * instances from postgres database.
 * 
 * @author jamil.ahmad
 *
 */
@RestController
@RequestMapping(value = "/config")
public class MigrateDeprecateVirtualCatalogController extends BaseController implements IRuntimeController {
  
  @Autowired
  private IMigrateDeprecateVirtualCatalog migratedeprecateVirtualCatalog;
  
  @RequestMapping(value = "/migratedeprecatevirtualcatalog", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(migratedeprecateVirtualCatalog.execute(new VoidModel()));
  }
}
