package com.cs.ui.config.controller.usecase.downloadtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.downloadtracker.GetDownloadLogsFilterDataRequestModel;
import com.cs.core.config.interactor.usecase.downloadtracker.IGetDownloadLogsFilterData;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is responsible for getting download logs filter data
 * @author vannya.kalani
 *
 */
@RestController
@RequestMapping(value = "/config")
public class GetDownloadLogsFilterDataController extends BaseController implements IConfigController {

  @Autowired
  IGetDownloadLogsFilterData getDownloadLogsFilterData;

  @RequestMapping(value = "/downloadlogs/filterdata", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetDownloadLogsFilterDataRequestModel model) throws Exception
  {
    return createResponse(getDownloadLogsFilterData.execute(model));
  }

}
