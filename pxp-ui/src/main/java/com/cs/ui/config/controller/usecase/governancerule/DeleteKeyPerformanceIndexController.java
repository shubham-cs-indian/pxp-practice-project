package com.cs.ui.config.controller.usecase.governancerule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.governancerule.IDeleteKeyPerformanceIndex;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class DeleteKeyPerformanceIndexController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IDeleteKeyPerformanceIndex deleteKPI;
  
  @RequestMapping(value = "/keyperformanceindex", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel model) throws Exception
  {
    
    return createResponse(deleteKPI.execute(model));
  }
}
