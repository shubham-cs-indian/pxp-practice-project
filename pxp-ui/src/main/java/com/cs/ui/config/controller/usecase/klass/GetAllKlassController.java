package com.cs.ui.config.controller.usecase.klass;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.klass.IGetAllKlasses;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllKlassController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllKlasses getAllKlasses;
  
  @RequestMapping(value = "/klasses", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllKlasses.execute(null));
  }
}
