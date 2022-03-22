package com.cs.ui.runtime.controller.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.runtime.interactor.model.instancetree.DefaultTypesRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetDefaultTypes;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetDefaultTypesController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetDefaultTypes getDefaultTypes;
  
  @RequestMapping(value = "/defaulttypes/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody DefaultTypesRequestModel dataModel) throws Exception
  {
    
    return createResponse(getDefaultTypes.execute(dataModel));
  }
}
