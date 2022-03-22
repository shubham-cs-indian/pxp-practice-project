package com.cs.ui.runtime.controller.usecase.dashboardtabs;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.runtime.interactor.usecase.dashboardtab.IGetAllDashboardTabs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAllDashboardTabsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllDashboardTabs getAllDashboardTabs;
  
  @RequestMapping(value = "/dashboardtab", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllDashboardTabs.execute(null));
  }
}
