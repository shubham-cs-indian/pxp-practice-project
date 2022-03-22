package com.cs.ui.config.controller.usecase.portal;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.portal.IGetPortals;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class GetPortalsController extends BaseController implements IConfigController {
  
  @Autowired
  public IGetPortals getPortals;
  
  @RequestMapping(value = "/getportals", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getPortals.execute(null));
  }
}
