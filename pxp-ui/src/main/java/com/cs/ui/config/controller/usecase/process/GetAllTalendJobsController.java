package com.cs.ui.config.controller.usecase.process;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.di.runtime.interactor.process.IGetAllTalendJobs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config/dataintegration")
public class GetAllTalendJobsController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetAllTalendJobs getAllTalendJobs;
  
  @RequestMapping(value = "/alltalendjobs", method = RequestMethod.POST)
  public IRESTModel getAllTalendJobList(
      @RequestParam(required = false, defaultValue = "all") String mode) throws Exception
  {
    return createResponse(getAllTalendJobs.execute(null));
  }
}
