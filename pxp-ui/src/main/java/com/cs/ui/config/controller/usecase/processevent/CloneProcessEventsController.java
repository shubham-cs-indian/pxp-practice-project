package com.cs.ui.config.controller.usecase.processevent;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.ConfigCloneEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.interactor.processevent.ICloneProcessEvents;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CloneProcessEventsController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICloneProcessEvents cloneProcessEvent;
  
  @RequestMapping(value = "/processevents/clone", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<ConfigCloneEntityInformationModel> processEventModel) throws Exception
  {
    IListModel<IConfigCloneEntityInformationModel> processesListModel = new ListModel<>();
    processesListModel.setList(processEventModel);
    return createResponse(cloneProcessEvent.execute(processesListModel));
  }
}
