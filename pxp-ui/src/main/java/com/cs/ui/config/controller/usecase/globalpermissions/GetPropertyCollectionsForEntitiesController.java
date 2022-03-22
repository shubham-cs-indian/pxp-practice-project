package com.cs.ui.config.controller.usecase.globalpermissions;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.globalpermissions.GetPropertyCollectionsForEntityModel;
import com.cs.core.config.interactor.usecase.globalpermissions.IGetPropertyCollectionsForEntity;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetPropertyCollectionsForEntitiesController extends BaseController
    implements IConfigController {
  
  @Autowired
  IGetPropertyCollectionsForEntity getPropertyCollectionsForEntity;
  
  @RequestMapping(value = "/globalpermissions/propertycollections", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetPropertyCollectionsForEntityModel model)
      throws Exception
  {
    
    return createResponse(getPropertyCollectionsForEntity.execute(model));
  }
}
