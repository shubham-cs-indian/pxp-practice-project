package com.cs.ui.config.controller.usecase.configdata;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.GetConfigEntityIdsCodeNamesRequestModel;
import com.cs.core.config.interactor.usecase.configdata.IGetConfigEntityIdsCodeNamesInteractor;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetConfigEntityIdsCodeNamesController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetConfigEntityIdsCodeNamesInteractor getConfigEntityIdsCodeNamesInteractor;
  
  @RequestMapping(value = "/getidsbycodenames", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetConfigEntityIdsCodeNamesRequestModel model)
      throws Exception
  {
    return createResponse(getConfigEntityIdsCodeNamesInteractor.execute(model));
  }
}
