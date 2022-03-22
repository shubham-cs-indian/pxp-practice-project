package com.cs.ui.config.controller.usecase.system;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.system.CreateSystemModel;
import com.cs.core.config.interactor.usecase.system.ICreateSystem;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateSystemController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateSystem createSystem;
  
  @RequestMapping(value = "/system", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateSystemModel condition) throws Exception
  {
    return createResponse(createSystem.execute(condition));
  }
}
