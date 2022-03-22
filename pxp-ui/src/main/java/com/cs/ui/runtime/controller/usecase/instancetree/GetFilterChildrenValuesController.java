package com.cs.ui.runtime.controller.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetFilterChildrenValues;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetFilterChildrenValuesController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetFilterChildrenValues getFilterChildrenValues;
  
  @RequestMapping(value = "/filterchildren/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetFilterChildrenRequestModel requestModel) throws Exception
  {
    return createResponse(getFilterChildrenValues.execute(requestModel));
  }
}
