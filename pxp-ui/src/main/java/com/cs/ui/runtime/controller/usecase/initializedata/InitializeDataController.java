package com.cs.ui.runtime.controller.usecase.initializedata;

import com.cs.core.interactor.usecase.initialize.IInitializeData;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class InitializeDataController extends BaseController {
  
  @Autowired
  IInitializeData initializeData;
  
  @RequestMapping(value = "/datainitialization", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody VoidModel voidModel) throws Exception
  {
    return createResponse(initializeData.execute(voidModel));
  }
}
