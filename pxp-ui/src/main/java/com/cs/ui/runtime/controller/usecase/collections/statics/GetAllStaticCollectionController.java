package com.cs.ui.runtime.controller.usecase.collections.statics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IGetAllStaticCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetAllStaticCollectionController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetAllStaticCollection getAllStaticCollection;
  
  @RequestMapping(value = "/staticcollections", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    IIdParameterModel model = new IdParameterModel("-1");
    return createResponse(getAllStaticCollection.execute(model));
  }

}