package com.cs.ui.runtime.controller.usecase.collections.statics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetFilterChildrenForCollectionRequestModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IGetFilterChildrenValuesForCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetFilterChildrenValuesForCollectionController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetFilterChildrenValuesForCollection getFilterChildrenValuesForCollection;
  
  @RequestMapping(value = "/collection/filterchildren", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetFilterChildrenForCollectionRequestModel requestModel) throws Exception
  {
    return createResponse(getFilterChildrenValuesForCollection.execute(requestModel));
  }

}
