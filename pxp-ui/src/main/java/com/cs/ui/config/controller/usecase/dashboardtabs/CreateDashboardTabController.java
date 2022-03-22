package com.cs.ui.config.controller.usecase.dashboardtabs;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.dashboardtabs.DashboardTabModel;
import com.cs.core.config.interactor.usecase.dashboardtab.ICreateDashboardTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateDashboardTabController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateDashboardTab createDashboardTab;
  
  @RequestMapping(value = "/dashboardtab", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody DashboardTabModel condition) throws Exception
  {
    return createResponse(createDashboardTab.execute(condition));
  }
}
