package com.cs.ui.config.controller.usecase.attribute;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.attribute.IGetAllStandardAttributes;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllStandardAttributesController extends BaseController
    implements IConfigController {
  
  @Autowired
  IGetAllStandardAttributes getAllStandardProperties;
  
  @RequestMapping(value = "/attributes/standard", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    
    return createResponse(getAllStandardProperties.execute(null));
  }
}
