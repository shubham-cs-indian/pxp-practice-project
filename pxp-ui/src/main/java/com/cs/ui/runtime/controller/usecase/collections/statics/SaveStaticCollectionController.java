package com.cs.ui.runtime.controller.usecase.collections.statics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.collections.SaveStaticCollectionModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.ISaveStaticCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class SaveStaticCollectionController extends BaseController implements IRuntimeController {
  
  @Autowired
  ISaveStaticCollection saveStaticCollection;
  
  @RequestMapping(value = "/staticcollections", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveStaticCollectionModel saveStaticCollectionModel) throws Exception
  {
    return createResponse(saveStaticCollection.execute(saveStaticCollectionModel));
  }
}
