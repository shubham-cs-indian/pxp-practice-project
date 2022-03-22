package com.cs.ui.runtime.controller.usecase.collections.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.collections.DynamicCollectionModel;
import com.cs.pim.runtime.interactor.usecase.dynamiccollection.ICreateDynamicCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class CreateDynamicCollectionController extends BaseController implements IRuntimeController {
  
  @Autowired
  ICreateDynamicCollection createDynamicCollection;
  
  @RequestMapping(value = "/dynamiccollections", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody DynamicCollectionModel dynamicCollectionModel) throws Exception
  {
    return createResponse(createDynamicCollection.execute(dynamicCollectionModel));
  }

}
