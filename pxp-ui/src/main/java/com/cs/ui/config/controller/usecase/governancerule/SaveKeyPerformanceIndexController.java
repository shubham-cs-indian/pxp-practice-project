package com.cs.ui.config.controller.usecase.governancerule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.governancerule.SaveKeyPerformanceIndexModel;
import com.cs.core.config.interactor.usecase.governancerule.ISaveKeyPerformanceIndex;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveKeyPerformanceIndexController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveKeyPerformanceIndex saveKeyPerformanceIndex;
  
  @RequestMapping(value = "/keyperformanceindex", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveKeyPerformanceIndexModel saveDataRuleModel)
      throws Exception
  {
    return createResponse(saveKeyPerformanceIndex.execute(saveDataRuleModel));
  }
}
