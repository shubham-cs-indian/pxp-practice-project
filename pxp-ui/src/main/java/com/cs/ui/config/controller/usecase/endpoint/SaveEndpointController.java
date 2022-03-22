package com.cs.ui.config.controller.usecase.endpoint;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.interactor.model.endpoint.SaveEndpointModel;
import com.cs.di.config.interactor.endpoint.ISaveEndpoint;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/config")
public class SaveEndpointController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveEndpoint saveEndpoint;
  
  @RequestMapping(value = "/endpoints", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<SaveEndpointModel> saveEndpointModel)
      throws Exception
  {
    IListModel<ISaveEndpointModel> endpointsListSaveModel = new ListModel<>();
    endpointsListSaveModel.setList(saveEndpointModel);
    return createResponse(saveEndpoint.execute(endpointsListSaveModel));
  }
}
