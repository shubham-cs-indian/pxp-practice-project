package com.cs.ui.config.controller.usecase.mapping;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.di.config.interactor.mappings.ICreateMapping;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateMappingController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateMapping createMapping;
  
  @RequestMapping(value = "/mappings", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody MappingModel mappingModel) throws Exception
  {
    
    return createResponse(createMapping.execute(mappingModel));
  }
}
