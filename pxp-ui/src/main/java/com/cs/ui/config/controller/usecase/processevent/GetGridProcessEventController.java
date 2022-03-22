package com.cs.ui.config.controller.usecase.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.WorkflowGridConfigGetAllRequestModel;
import com.cs.di.config.interactor.processevent.IGetGridProcessEvents;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetGridProcessEventController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetGridProcessEvents getGridProcessEvents;
  
  @RequestMapping(value = "/processevents/grid", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody WorkflowGridConfigGetAllRequestModel getAllModel) throws Exception
  {
    return createResponse(getGridProcessEvents.execute(getAllModel));
  }
}
