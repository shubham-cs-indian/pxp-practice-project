package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IMigrateIconsForIconLibrary;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * 
 * Description:- Controller for migrating icons for icon library.
 * @author rahul.sehrawat
 *
 */
@RestController
@RequestMapping
public class MigrateIconsForIconLibraryController extends BaseController {
  
  @Autowired
  IMigrateIconsForIconLibrary migrateIconsOfIconLibrary;
  
  @RequestMapping(value = "/migrateicons", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(migrateIconsOfIconLibrary.execute(new VoidModel()));
  }
}
