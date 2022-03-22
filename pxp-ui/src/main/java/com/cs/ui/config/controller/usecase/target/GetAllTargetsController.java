package com.cs.ui.config.controller.usecase.target;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.target.IGetAllTargets;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllTargetsController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllTargets getAllTargets;
  
  @RequestMapping(value = "/targets", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllTargets.execute(null));
  }
}
