package com.cs.ui.config.controller.usecase.state;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.state.IGetAllStates;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllStatesController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllStates getAllStates;
  
  @RequestMapping(value = "/states", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllStates.execute(null));
  }
}
