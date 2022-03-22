package com.cs.ui.config.controller.usecase.propertycollection;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.propertycollection.PropertyCollectionModel;
import com.cs.core.config.interactor.usecase.propertycollection.ICreatePropertyCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreatePropertyCollectionController extends BaseController
    implements IConfigController {
  
  @Autowired
  ICreatePropertyCollection createPropertyCollection;
  
  @RequestMapping(value = "/propertycollections", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody PropertyCollectionModel propertyCollectionModel)
      throws Exception
  {
    
    return createResponse(createPropertyCollection.execute(propertyCollectionModel));
  }
}
