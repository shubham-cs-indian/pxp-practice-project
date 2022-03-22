package com.cs.ui.config.controller.usecase.propertycollection;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.propertycollection.SavePropertyCollectionModel;
import com.cs.core.config.interactor.usecase.propertycollection.ISavePropertyCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SavePropertyCollectionController extends BaseController implements IConfigController {
  
  @Autowired
  ISavePropertyCollection savePropertyCollection;
  
  @RequestMapping(value = "/propertycollections", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SavePropertyCollectionModel model) throws Exception
  {
    return createResponse(savePropertyCollection.execute(model));
  }
}
