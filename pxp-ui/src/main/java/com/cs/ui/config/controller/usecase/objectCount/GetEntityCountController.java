package com.cs.ui.config.controller.usecase.objectCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.objectCount.GetEntityTypeRequestModel;
import com.cs.core.config.interactor.usecase.objectCount.IGetEnityCount;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetEntityCountController extends BaseController implements IRuntimeController{
  
  @Autowired
  IGetEnityCount getEntityCount;
  
  @RequestMapping(value = "/getObjectCount", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetEntityTypeRequestModel getEntityTypeRequestModel) throws Exception
  {
    return createResponse(getEntityCount.execute(getEntityTypeRequestModel));
  }
  
}
