package com.cs.ui.runtime.controller.usecase.dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.dashboard.DashboardBGPStatusRequestModel;
import com.cs.core.runtime.interactor.usecase.dashboard.IGetDashboardTileForBGPStatus;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetDashboardTileForBGPStatusController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetDashboardTileForBGPStatus getDashboardTileForBGPStatus;
  
  @RequestMapping(value = "/dashboardtile/bgpstatusinfo", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody DashboardBGPStatusRequestModel model) throws Exception
  {
     return createResponse(getDashboardTileForBGPStatus.execute(model));
  }
}
