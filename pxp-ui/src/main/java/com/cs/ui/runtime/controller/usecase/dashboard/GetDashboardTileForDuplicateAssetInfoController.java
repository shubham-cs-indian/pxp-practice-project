package com.cs.ui.runtime.controller.usecase.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.dashboard.DashboardInformationRequestModel;
import com.cs.dam.runtime.interactor.usecase.duplicatedetection.IGetDashboardTileForDuplicateAssetInfo;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * Controller to get duplicate assets for dam dashboard
 * @author mrunali.dhenge
 *
 */
@RestController
@RequestMapping(value = "/runtime")
public class GetDashboardTileForDuplicateAssetInfoController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetDashboardTileForDuplicateAssetInfo getDashboardTileForDuplicateAssetInfo;
  
  @RequestMapping(value = "/dashboardtile/duplicateasset/info", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody DashboardInformationRequestModel model) throws Exception
  {
    return createResponse(getDashboardTileForDuplicateAssetInfo.execute(model));
  }
}
