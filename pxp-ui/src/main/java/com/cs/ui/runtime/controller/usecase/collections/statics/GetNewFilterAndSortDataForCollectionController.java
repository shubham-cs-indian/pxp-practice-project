package com.cs.ui.runtime.controller.usecase.collections.statics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instancetree.GetNewFilterAndSortDataForCollectionRequestModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IGetNewFilterAndSortDataForCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetNewFilterAndSortDataForCollectionController extends BaseController {
  
  @Autowired
  protected IGetNewFilterAndSortDataForCollection getNewFilterAndSortDataForCollection;
  
  @RequestMapping(value = "/collection/sortandfilter", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetNewFilterAndSortDataForCollectionRequestModel model) throws Exception
  {
    return createResponse(getNewFilterAndSortDataForCollection.execute(model));
  }
}
