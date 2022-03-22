package com.cs.ui.config.controller.usecase.governancerule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.governancerule.CreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.usecase.governancerule.ICreateKeyPerformanceIndex;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateKeyPerfermanceIndexController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ICreateKeyPerformanceIndex createKeyPerformanceIndex;
  
  @RequestMapping(value = "/keyperformanceindex", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateKeyPerformanceIndexModel requestModel)
      throws Exception
  {
    return createResponse(createKeyPerformanceIndex.execute(requestModel));
  }
}
