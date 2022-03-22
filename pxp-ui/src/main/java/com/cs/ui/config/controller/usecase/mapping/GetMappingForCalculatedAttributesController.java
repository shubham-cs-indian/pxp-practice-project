package com.cs.ui.config.controller.usecase.mapping;

import com.cs.core.config.interactor.usecase.calculatedattribute.IGetMappingForCalculatedAttributes;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetMappingForCalculatedAttributesController extends BaseController {
  
  @Autowired
  protected IGetMappingForCalculatedAttributes getMappingForCalculatedAttributes;
  
  @RequestMapping(value = "/mapping/calculatedattribute", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getMappingForCalculatedAttributes.execute(null));
  }
}
