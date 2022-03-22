package com.cs.ui.config.controller.usecase.downloadtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.downloadtracker.GetDownloadLogsRequestModel;
import com.cs.core.config.interactor.usecase.downloadtracker.IGetDownloadLogs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetDownloadLogsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetDownloadLogs getDownloadLogs;
  
  @RequestMapping(value = "/downloadlogs", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetDownloadLogsRequestModel model) throws Exception
  {
    return createResponse(getDownloadLogs.execute(model));
  }
  
}
