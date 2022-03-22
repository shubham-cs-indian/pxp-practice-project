package com.cs.ui.runtime.controller.usecase.assetinstance.downloadtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.dam.runtime.interactor.usecase.downloadtracker.IGetDownloadCountForOverviewTab;
import com.cs.runtime.interactor.model.downloadtracker.GetDownloadCountRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetDownloadCountForOverviewTabController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IGetDownloadCountForOverviewTab getDownloadCountForOverviewTab;
  
  @RequestMapping(value = "/downloadcount", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetDownloadCountRequestModel model) throws Exception
  {
    return createResponse(getDownloadCountForOverviewTab.execute(model));
  }
  
}
