package com.cs.ui.config.controller.usecase.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.mapping.OutBoundMappingModel;
import com.cs.di.config.interactor.mappings.ICreateOutBoundMapping;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CreateOutBoundMappingController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateOutBoundMapping createOutBoundMapping;
  
  @RequestMapping(value = "/outBoundMapping", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody OutBoundMappingModel mappingModel) throws Exception
  {
    return createResponse(createOutBoundMapping.execute(mappingModel));
  }
}
