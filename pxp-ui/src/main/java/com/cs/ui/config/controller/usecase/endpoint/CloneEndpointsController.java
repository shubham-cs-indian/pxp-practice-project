package com.cs.ui.config.controller.usecase.endpoint;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.CloneEndpointModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;
import com.cs.core.config.interactor.usecase.endpoint.ICloneEndpoints;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CloneEndpointsController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICloneEndpoints cloneEndpoints;
  
  @RequestMapping(value = "/endpoints/clone", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<CloneEndpointModel> endpointCloneModelsList)
      throws Exception
  {
    IListModel<ICloneEndpointModel> listModel = new ListModel<ICloneEndpointModel>();
    listModel.setList(endpointCloneModelsList);
    return createResponse(cloneEndpoints.execute(listModel));
  }
}
