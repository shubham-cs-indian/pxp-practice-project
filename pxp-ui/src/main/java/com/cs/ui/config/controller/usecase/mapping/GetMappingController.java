package com.cs.ui.config.controller.usecase.mapping;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.mapping.GetOutAndInboundMappingModel;
import com.cs.core.config.interactor.usecase.mapping.IGetMapping;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetMappingController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetMapping getMapping;
  
  @RequestMapping(value = "/getMappings", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetOutAndInboundMappingModel inboundMappingModel) throws Exception
  {
    //IIdParameterModel getEntityModel = new IdParameterModel(id);
    
    return createResponse(getMapping.execute(inboundMappingModel));
  }
}
