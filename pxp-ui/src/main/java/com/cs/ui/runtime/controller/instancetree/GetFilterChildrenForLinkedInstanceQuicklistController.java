package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetFilterChildrenForLIQRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetFilterChildrenForLinkedInstanceQuicklist;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class GetFilterChildrenForLinkedInstanceQuicklistController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetFilterChildrenForLinkedInstanceQuicklist getFilterChildrenForLinkedInstanceQuicklist;
  
  @RequestMapping(value = "/linkedinstance/quicklist/filterchildren", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetFilterChildrenForLIQRequestModel requestModel) throws Exception
  {
    return createResponse(getFilterChildrenForLinkedInstanceQuicklist.execute(requestModel));
  }
}
