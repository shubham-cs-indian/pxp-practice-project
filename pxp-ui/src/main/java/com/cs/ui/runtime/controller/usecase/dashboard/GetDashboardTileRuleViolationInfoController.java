package com.cs.ui.runtime.controller.usecase.dashboard;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.dashboard.DashboardInformationRequestModel;
import com.cs.core.runtime.interactor.usecase.dashboard.IGetDashboardTileRuleViolationInfo;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetDashboardTileRuleViolationInfoController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetDashboardTileRuleViolationInfo getDashboardTileRuleViolationInfo;
  
  @RequestMapping(value = "/dashboardtile/ruleviolationinfo", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody DashboardInformationRequestModel model) throws Exception
  {
    
    return createResponse(getDashboardTileRuleViolationInfo.execute(model));
  }
}
