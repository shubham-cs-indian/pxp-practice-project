package com.cs.ui.runtime.controller.usecase.collections.statics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IDeleteStaticCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class DeleteStaticCollectionController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IDeleteStaticCollection deleteStaticCollection;
  
  @RequestMapping(value = "/staticcollections", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel ideleteModel) throws Exception
  {
    return createResponse(deleteStaticCollection.execute(ideleteModel));
  }
}
