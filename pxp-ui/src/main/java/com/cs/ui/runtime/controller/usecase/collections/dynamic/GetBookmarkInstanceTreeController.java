package com.cs.ui.runtime.controller.usecase.collections.dynamic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetInstanceTreeForBookmarkRequestModel;
import com.cs.pim.runtime.interactor.usecase.dynamiccollection.IGetBookmarkInstanceTree;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetBookmarkInstanceTreeController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetBookmarkInstanceTree getBookmarkInstanceTree;
  
  @RequestMapping(value = "/bookmark/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetInstanceTreeForBookmarkRequestModel requestModel)
      throws Exception
  {
    return createResponse(getBookmarkInstanceTree.execute(requestModel));
  }
}
