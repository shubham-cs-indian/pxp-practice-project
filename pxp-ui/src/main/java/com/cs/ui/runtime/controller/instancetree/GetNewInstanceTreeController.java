package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetNewInstanceTree;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetNewInstanceTreeController extends BaseController implements IRuntimeController {
  
  @Autowired
  private IGetNewInstanceTree getNewInstanceTree;
  
  @RequestMapping(value = "/instancetree/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetInstanceTreeRequestModel requestModel) throws Exception
  {
    return createResponse(getNewInstanceTree.execute(requestModel));
  }
}
