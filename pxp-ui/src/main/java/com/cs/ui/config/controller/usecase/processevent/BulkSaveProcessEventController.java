package com.cs.ui.config.controller.usecase.processevent;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.config.interactor.model.initializeworflowevent.SaveProcessEventModel;
import com.cs.di.config.interactor.processevent.IBulkSaveProcessEvent;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class BulkSaveProcessEventController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveProcessEvent bulkSaveProcessEvent;
  
  @RequestMapping(value = "/processevents/bulksave", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<SaveProcessEventModel> saveProcessEventModel)
      throws Exception
  {
    IListModel<ISaveProcessEventModel> processesListSaveModel = new ListModel<>();
    processesListSaveModel.setList(saveProcessEventModel);
    return createResponse(bulkSaveProcessEvent.execute(processesListSaveModel));
  }
}
