package com.cs.ui.runtime.controller.usecase.collections.statics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetStaticCollectionInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IGetCollectionInstanceTree;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetCollectionInstanceTreeController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetCollectionInstanceTree getCollectionInstanceTree;
  
  @RequestMapping(value = "/collection/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetStaticCollectionInstanceTreeRequestModel requestModel) throws Exception
  {
    return createResponse(getCollectionInstanceTree.execute(requestModel));
  }
}