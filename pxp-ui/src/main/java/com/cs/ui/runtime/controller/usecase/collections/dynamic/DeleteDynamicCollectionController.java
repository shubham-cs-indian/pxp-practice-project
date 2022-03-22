package com.cs.ui.runtime.controller.usecase.collections.dynamic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.pim.runtime.interactor.usecase.dynamiccollection.IDeleteDynamicCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class DeleteDynamicCollectionController extends BaseController implements IRuntimeController {
  
  @Autowired
  IDeleteDynamicCollection deleteDynamicCollection;
  
  @RequestMapping(value = "/dynamiccollections", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel ideleteModel) throws Exception
  {
    return createResponse(deleteDynamicCollection.execute(ideleteModel));
  }
}
