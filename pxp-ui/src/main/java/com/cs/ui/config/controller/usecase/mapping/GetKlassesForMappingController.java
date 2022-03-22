package com.cs.ui.config.controller.usecase.mapping;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.config.interactor.mappings.IGetKlassesForMapping;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetKlassesForMappingController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetKlassesForMapping getKlassesForMapping;
  
  @RequestMapping(value = "/klassesForMapping", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getKlassesForMapping.execute(null));
  }
}
