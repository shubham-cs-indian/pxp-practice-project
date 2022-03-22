package com.cs.ui.config.controller.usecase.system;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.usecase.system.IGetPaginatedSystems;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetPaginatedSystemsController extends BaseController implements IConfigController {
  
  @Autowired
  IGetPaginatedSystems getPaginatedSystems;
  
  @RequestMapping(value = "/systems/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel condition) throws Exception
  {
    return createResponse(getPaginatedSystems.execute(condition));
  }
}
