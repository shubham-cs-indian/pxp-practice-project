package com.cs.ui.config.controller.usecase.migration;

import com.cs.core.config.interactor.usecase.migration.IMigrateCoupledRecords;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MigrateCoupledRecordsController extends BaseController {
  
  @Autowired
  protected IMigrateCoupledRecords migrateCoupledRecords;
  
  @RequestMapping(value = "/migratecoupledrecords", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(migrateCoupledRecords.execute(new VoidModel()));
  }
}
